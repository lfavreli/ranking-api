package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.dynamodb.*
import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.features.tournaments.model.TournamentPlayer
import io.ktor.server.plugins.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.QueryRequest

fun updatePlayerScoreHandler(tournamentId: String, playerId: String, newScore: Int, dynamoDbClient: DynamoDbClient): TournamentPlayer? {
    // 1. Check if the tournament exists
    val getTournamentRequest = GetItemRequest.builder()
        .tableName(TOURNAMENT_TABLE) // The table where tournaments are stored
        .key(mapOf(TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build()))
        .build()

    val tournamentItem = dynamoDbClient.getItem(getTournamentRequest).item()
    if (tournamentItem.isEmpty()) throw NotFoundException("Tournament not found")

    // 2. Get current player data
    val getPlayerRequest = GetItemRequest.builder()
        .tableName(PLAYER_TABLE)
        .key(mapOf(PLAYER_ID to AttributeValue.builder().s(playerId).build()))
        .build()

    val playerItem = dynamoDbClient.getItem(getPlayerRequest).item()
    if (playerItem.isEmpty()) throw NotFoundException("Player not found")

    val player = Player.fromDynamoDbItem(playerItem) ?: throw NotFoundException("Player not found")
    val displayName = player.displayName

    // 3. Directly update (or insert) the new score in Leaderboard
    val putNewScoreRequest = PutItemRequest.builder()
        .tableName(LEADERBOARD_TABLE)
        .item(
            mapOf(
                TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build(),
                PLAYER_ID to AttributeValue.builder().s(playerId).build(),
                LEADERBOARD_SCORE to AttributeValue.builder().n(newScore.toString()).build()
            )
        )
        .build()
    dynamoDbClient.putItem(putNewScoreRequest)

    // 4. Recalculate Rank using Scan
    val rank = getPlayerRankFromLeaderboard(tournamentId, newScore, dynamoDbClient)

    // 5. Return the updated TournamentPlayer object
    return TournamentPlayer(
        playerId = playerId,
        displayName = displayName,
        score = newScore,
        rank = rank
    )
}

fun getPlayerRankFromLeaderboard(tournamentId: String, score: Int, dynamoDbClient: DynamoDbClient): Int {
    val rankQuery = QueryRequest.builder()
        .tableName(LEADERBOARD_TABLE)
        .indexName("score-index") // Use the GSI for ranking
        .keyConditionExpression("tournamentId = :tournamentId") // Exact match required
        .expressionAttributeValues(
            mapOf(":tournamentId" to AttributeValue.builder().s(tournamentId).build())
        )
        .scanIndexForward(false) // Order from highest to lowest score
        .build()

    val rankResult = dynamoDbClient.query(rankQuery)

    // Count how many players have a higher score
    val rank = rankResult.items().count { (it[LEADERBOARD_SCORE]?.n()?.toInt() ?: 0) > score } + 1
    return rank
}
