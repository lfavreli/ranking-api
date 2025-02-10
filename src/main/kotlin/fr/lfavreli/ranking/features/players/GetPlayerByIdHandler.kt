package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.dynamodb.*
import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.features.players.model.PlayerTournament
import io.ktor.server.plugins.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.ScanRequest

fun getPlayerByIdHandler(playerId: String, dynamoDbClient: DynamoDbClient): Player? {
    // 1. Retrieve player details from Players table
    val getPlayerRequest = GetItemRequest.builder()
        .tableName(PLAYER_TABLE)
        .key(mapOf(PLAYER_ID to AttributeValue.builder().s(playerId).build()))
        .build()

    val playerItem = dynamoDbClient.getItem(getPlayerRequest).item()
    if (playerItem.isEmpty()) throw NotFoundException("Player not found")

    val player = Player.fromDynamoDbItem(playerItem) ?: throw NotFoundException("Player not found")

    // 2. Fetch player's tournament scores and rankings from Leaderboard
    val playerTournaments = getPlayerTournamentsFromLeaderboard(playerId, dynamoDbClient)

    // 3. Return updated player object with real-time rankings
    return player.copy(tournaments = playerTournaments)
}

fun getPlayerTournamentsFromLeaderboard(playerId: String, dynamoDbClient: DynamoDbClient): List<PlayerTournament> {
    val tournaments = mutableListOf<PlayerTournament>()

    // Scan for all Player tournaments
    val scanRequest = ScanRequest.builder()
        .tableName(LEADERBOARD_TABLE)
        .filterExpression("playerId = :playerId")
        .expressionAttributeValues(
            mapOf(":playerId" to AttributeValue.builder().s(playerId).build())
        )
        .build()

    val leaderboardResults = dynamoDbClient.scan(scanRequest).items()

    leaderboardResults.forEach { item ->
        val tournamentId = item[TOURNAMENT_ID]?.s() ?: return@forEach
        val score = item[LEADERBOARD_SCORE]?.n()?.toInt() ?: return@forEach
        val rank = getPlayerRankFromLeaderboard(tournamentId, score, dynamoDbClient)

        tournaments.add(PlayerTournament(tournamentId, score, rank))
    }

    return tournaments
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
