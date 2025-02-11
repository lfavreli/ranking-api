package fr.lfavreli.ranking.repository

import fr.lfavreli.ranking.features.dynamodb.*
import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.features.tournaments.model.LeaderboardEntry
import org.koin.core.annotation.Single
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Single
class LeaderboardRepository(private val dynamoDBRepository: DynamoDBRepository) {

    fun saveScore(tournamentId: String, player: Player, newScore: Int) {
        dynamoDBRepository.putItem(
            tableName = LEADERBOARD_TABLE,
            item = mapOf(
                TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build(),
                PLAYER_ID to AttributeValue.builder().s(player.playerId).build(),
                LEADERBOARD_SCORE to AttributeValue.builder().n(newScore.toString()).build(),
                DISPLAY_NAME to AttributeValue.builder().s(player.displayName).build()
            )
        )
    }

    fun getPlayerRank(tournamentId: String, score: Int): Int {
        val leaderboardResult = dynamoDBRepository.query(
            tableName = LEADERBOARD_TABLE,
            indexName = LEADERBOARD_SCORE_INDEX,
            keyConditionExpression = "$TOURNAMENT_ID = :tournamentId AND $LEADERBOARD_SCORE > :score",
            expressionAttributeValues = mapOf(
                ":tournamentId" to AttributeValue.builder().s(tournamentId).build(),
                ":score" to AttributeValue.builder().n(score.toString()).build()
            )
        )
        // Rank is 1 + the number of players with a higher score
        return leaderboardResult.size + 1
    }

    fun getLeaderboard(tournamentId: String): List<LeaderboardEntry> {
        val leaderboardEntries = getLeaderboardEntries(tournamentId)
        return leaderboardEntries.mapIndexedNotNull { index, item ->
            LeaderboardEntry.fromDynamoDbItem(item, index + 1)
        }
    }

    fun deleteTournamentPlayers(tournamentId: String) {
        val leaderboardEntries = getLeaderboardEntries(tournamentId)

        // Delete each leaderboard entry
        leaderboardEntries.forEach { item ->
            val playerId = item[PLAYER_ID]?.s() ?: return@forEach
            dynamoDBRepository.deleteItem(
                tableName = LEADERBOARD_TABLE,
                key = mapOf(
                    TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build(),
                    PLAYER_ID to AttributeValue.builder().s(playerId).build()
                )
            )
        }
    }

    private fun getLeaderboardEntries(tournamentId: String): MutableList<MutableMap<String, AttributeValue>> {
        return dynamoDBRepository.query(
            tableName = LEADERBOARD_TABLE,
            indexName = LEADERBOARD_SCORE_INDEX,
            keyConditionExpression = "$TOURNAMENT_ID = :tournamentId",
            expressionAttributeValues = mapOf(":tournamentId" to AttributeValue.builder().s(tournamentId).build()),
            scanIndexForward = false // Ensures scores are sorted from highest to lowest
        )
    }
}
