package fr.lfavreli.ranking.repository

import fr.lfavreli.ranking.features.dynamodb.*
import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.features.tournaments.model.LeaderboardEntry
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.QueryRequest

object LeaderboardRepository {

    fun saveScore(tournamentId: String, player: Player, newScore: Int, dynamoDbClient: DynamoDbClient) {
        DynamoDBOperations.putItem(
            tableName = LEADERBOARD_TABLE,
            item = mapOf(
                TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build(),
                PLAYER_ID to AttributeValue.builder().s(player.playerId).build(),
                LEADERBOARD_SCORE to AttributeValue.builder().n(newScore.toString()).build(),
                DISPLAY_NAME to AttributeValue.builder().s(player.displayName).build()
            ),
            client = dynamoDbClient
        )
    }

    fun getPlayerRank(tournamentId: String, score: Int, dynamoDbClient: DynamoDbClient): Int {
        val leaderboardResult = DynamoDBOperations.query(
            tableName = LEADERBOARD_TABLE,
            indexName = LEADERBOARD_SCORE_INDEX,
            keyConditionExpression = "$TOURNAMENT_ID = :tournamentId AND $LEADERBOARD_SCORE > :score",
            expressionAttributeValues = mapOf(
                ":tournamentId" to AttributeValue.builder().s(tournamentId).build(),
                ":score" to AttributeValue.builder().n(score.toString()).build()
            ),
            client = dynamoDbClient
        )
        // Rank is 1 + the number of players with a higher score
        return leaderboardResult.size + 1
    }

    fun getLeaderboard(tournamentId: String, dynamoDbClient: DynamoDbClient): List<LeaderboardEntry> {
        val leaderboardResult = DynamoDBOperations.query(
            tableName = LEADERBOARD_TABLE,
            indexName = LEADERBOARD_SCORE_INDEX,
            keyConditionExpression = "$TOURNAMENT_ID = :tournamentId",
            expressionAttributeValues = mapOf(":tournamentId" to AttributeValue.builder().s(tournamentId).build()),
            scanIndexForward = false, // Ensures scores are sorted from highest to lowest
            client = dynamoDbClient
        )
        return leaderboardResult.mapIndexedNotNull { index, item ->
            LeaderboardEntry.fromDynamoDbItem(item, index + 1)
        }
    }

    fun deleteTournamentPlayers(tournamentId: String, dynamoDbClient: DynamoDbClient) {
        // Query to get all leaderboard entries for the tournament
        val queryRequest = QueryRequest.builder()
            .tableName(LEADERBOARD_TABLE)
            .keyConditionExpression("$TOURNAMENT_ID = :tournamentId")
            .expressionAttributeValues(
                mapOf(":tournamentId" to AttributeValue.builder().s(tournamentId).build())
            )
            .build()

        val leaderboardEntries = dynamoDbClient.query(queryRequest).items()

        // Delete each leaderboard entry
        leaderboardEntries.forEach { item ->
            val playerId = item["playerId"]?.s() ?: return@forEach
            val deleteRequest = DeleteItemRequest.builder()
                .tableName(LEADERBOARD_TABLE)
                .key(
                    mapOf(
                        TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build(),
                        "playerId" to AttributeValue.builder().s(playerId).build()
                    )
                )
                .build()
            dynamoDbClient.deleteItem(deleteRequest)
        }
    }
}
