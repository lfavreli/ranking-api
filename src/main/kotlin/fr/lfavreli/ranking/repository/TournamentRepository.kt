package fr.lfavreli.ranking.repository

import fr.lfavreli.ranking.features.dynamodb.TOURNAMENT_ID
import fr.lfavreli.ranking.features.dynamodb.TOURNAMENT_TABLE
import fr.lfavreli.ranking.features.tournaments.model.Tournament
import io.ktor.server.plugins.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

object TournamentRepository {

    fun fetchAll(dynamoDbClient: DynamoDbClient): List<Tournament> {
        val tournaments = DynamoDBOperations.scan(TOURNAMENT_TABLE, dynamoDbClient)
        return tournaments.mapNotNull { Tournament.fromDynamoDbItem(it) }
    }

    fun getById(tournamentId: String, dynamoDbClient: DynamoDbClient): Tournament {
        val tournamentItem = DynamoDBOperations.getItem(
            tableName = TOURNAMENT_TABLE,
            key = mapOf(TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build()),
            client = dynamoDbClient
        )
        return Tournament.fromDynamoDbItem(tournamentItem) ?: throw NotFoundException("Player not found")
    }

    fun isTournamentExists(tournamentId: String, dynamoDbClient: DynamoDbClient): Boolean {
        val tournamentItem = DynamoDBOperations.getItem(
            tableName = TOURNAMENT_TABLE,
            key = mapOf(TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build()),
            client = dynamoDbClient
        )
        return tournamentItem.isNotEmpty()
    }

    fun save(tournament: Tournament, dynamoDbClient: DynamoDbClient) {
        val dynamoDbItem = Tournament.toDynamoDbItem(tournament)
        DynamoDBOperations.putItem(TOURNAMENT_TABLE, dynamoDbItem, dynamoDbClient)
    }
}
