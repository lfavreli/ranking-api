package fr.lfavreli.ranking.repository

import fr.lfavreli.ranking.features.dynamodb.TOURNAMENT_ID
import fr.lfavreli.ranking.features.dynamodb.TOURNAMENT_TABLE
import fr.lfavreli.ranking.features.tournaments.model.Tournament
import io.ktor.server.plugins.*
import org.koin.core.annotation.Single
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Single
class TournamentRepository(private val dynamoDBRepository: DynamoDBRepository) {

    fun fetchAll(): List<Tournament> {
        val tournaments = dynamoDBRepository.scan(TOURNAMENT_TABLE)
        return tournaments.mapNotNull { Tournament.fromDynamoDbItem(it) }
    }

    fun getById(tournamentId: String): Tournament {
        val tournamentItem = dynamoDBRepository.getItem(
            tableName = TOURNAMENT_TABLE,
            key = mapOf(TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build()),
        )
        return Tournament.fromDynamoDbItem(tournamentItem) ?: throw NotFoundException("Tournament not found")
    }

    fun isTournamentExists(tournamentId: String): Boolean {
        val tournamentItem = dynamoDBRepository.getItem(
            tableName = TOURNAMENT_TABLE,
            key = mapOf(TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build()),
        )
        return tournamentItem.isNotEmpty()
    }

    fun save(tournament: Tournament) {
        val dynamoDbItem = Tournament.toDynamoDbItem(tournament)
        dynamoDBRepository.putItem(TOURNAMENT_TABLE, dynamoDbItem)
    }
}
