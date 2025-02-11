package fr.lfavreli.ranking.repository

import fr.lfavreli.ranking.features.dynamodb.*
import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.features.players.model.PlayerTournament
import io.ktor.server.plugins.*
import org.koin.core.annotation.Single
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Single
class PlayerRepository(
    private val leaderboardRepository: LeaderboardRepository,
    private val dynamoDBRepository: DynamoDBRepository
) {

    fun fetchAll(): List<Player> {
        val players = dynamoDBRepository.scan(PLAYER_TABLE)
        return players.mapNotNull { Player.fromDynamoDbItem(it) }
    }

    fun getById(playerId: String): Player {
        val playerItem = dynamoDBRepository.getItem(
            tableName = PLAYER_TABLE,
            key = mapOf(PLAYER_ID to AttributeValue.builder().s(playerId).build())
        )
        return Player.fromDynamoDbItem(playerItem) ?: throw NotFoundException("Player not found")
    }

    fun getParticipatingTournaments(playerId: String): List<PlayerTournament> {
        val leaderboardItems = dynamoDBRepository.scan(
            tableName = LEADERBOARD_TABLE,
            filterExpression = "$PLAYER_ID = :playerId",
            expressionAttributeValues = mapOf(":playerId" to AttributeValue.builder().s(playerId).build())
        )
        return leaderboardItems.mapNotNull {
            val tournamentId = it[TOURNAMENT_ID]?.s() ?: return@mapNotNull null
            val score = it[LEADERBOARD_SCORE]?.n()?.toInt() ?: return@mapNotNull null

            val rank = leaderboardRepository.getPlayerRank(tournamentId, score)
            PlayerTournament(tournamentId, score, rank)
        }
    }

    fun save(player: Player) {
        val dynamoDbItem = Player.toDynamoDbItem(player)
        dynamoDBRepository.putItem(PLAYER_TABLE, dynamoDbItem)
    }
}
