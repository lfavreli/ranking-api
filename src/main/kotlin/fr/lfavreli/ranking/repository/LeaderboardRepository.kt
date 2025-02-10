package fr.lfavreli.ranking.repository

import fr.lfavreli.ranking.features.dynamodb.LEADERBOARD_SCORE
import fr.lfavreli.ranking.features.dynamodb.LEADERBOARD_TABLE
import fr.lfavreli.ranking.features.dynamodb.PLAYER_ID
import fr.lfavreli.ranking.features.dynamodb.TOURNAMENT_ID
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.QueryRequest

object LeaderboardRepository {

    fun saveScore(tournamentId: String, playerId: String, newScore: Int, dynamoDbClient: DynamoDbClient) {
        DynamoDBOperations.putItem(
            tableName = LEADERBOARD_TABLE,
            item = mapOf(
                TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build(),
                PLAYER_ID to AttributeValue.builder().s(playerId).build(),
                LEADERBOARD_SCORE to AttributeValue.builder().n(newScore.toString()).build()
            ),
            client = dynamoDbClient
        )
    }

    fun getPlayerRank(tournamentId: String, score: Int, dynamoDbClient: DynamoDbClient): Int {
        val leaderboardResult = DynamoDBOperations.query(
            tableName = LEADERBOARD_TABLE,
            indexName = "score-index",
            keyConditionExpression = "tournamentId = :tournamentId",
            expressionAttributeValues = mapOf(":tournamentId" to AttributeValue.builder().s(tournamentId).build()),
            scanIndexForward = false, // Ensures scores are sorted from highest to lowest.
            client = dynamoDbClient
        )
        // Count how many players have a higher score
        return leaderboardResult.items().count { (it[LEADERBOARD_SCORE]?.n()?.toInt() ?: 0) > score } + 1
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
