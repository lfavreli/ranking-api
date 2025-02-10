package fr.lfavreli.ranking.repository

import fr.lfavreli.ranking.features.dynamodb.*
import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.features.players.model.PlayerTournament
import io.ktor.server.plugins.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

object PlayerRepository {

    fun fetchAll(dynamoDbClient: DynamoDbClient): List<Player> {
        val players = DynamoDBOperations.scan(PLAYER_TABLE, dynamoDbClient)
        return players.mapNotNull { Player.fromDynamoDbItem(it) }
    }

    fun getById(playerId: String, dynamoDbClient: DynamoDbClient): Player {
        val playerItem = DynamoDBOperations.getItem(
            tableName = PLAYER_TABLE,
            key = mapOf(PLAYER_ID to AttributeValue.builder().s(playerId).build()),
            client = dynamoDbClient
        )
        return Player.fromDynamoDbItem(playerItem) ?: throw NotFoundException("Player not found")
    }

    fun getParticipatingTournaments(playerId: String, dynamoDbClient: DynamoDbClient): List<PlayerTournament> {
        val leaderboardItems = DynamoDBOperations.scan(
            tableName = LEADERBOARD_TABLE,
            client = dynamoDbClient,
            filterExpression = "playerId = :playerId",
            expressionAttributeValues = mapOf(":playerId" to AttributeValue.builder().s(playerId).build())
        )
        return leaderboardItems.mapNotNull {
            val tournamentId = it[TOURNAMENT_ID]?.s() ?: return@mapNotNull null
            val score = it[LEADERBOARD_SCORE]?.n()?.toInt() ?: return@mapNotNull null

            val rank = LeaderboardRepository.getPlayerRank(tournamentId, score, dynamoDbClient)
            PlayerTournament(tournamentId, score, rank)
        }
    }

    fun save(player: Player, dynamoDbClient: DynamoDbClient) {
        val dynamoDbItem = Player.toDynamoDbItem(player)
        DynamoDBOperations.putItem(PLAYER_TABLE, dynamoDbItem, dynamoDbClient)
    }
}
