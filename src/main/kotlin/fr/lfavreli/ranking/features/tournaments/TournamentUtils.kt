package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.repository.TournamentRepository
import io.ktor.server.plugins.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

object TournamentUtils {

    fun ensureTournamentExists(tournamentId: String, dynamoDbClient: DynamoDbClient) {
        if (!TournamentRepository.isTournamentExists(tournamentId, dynamoDbClient)) {
            throw NotFoundException("Tournament not found")
        }
    }
}