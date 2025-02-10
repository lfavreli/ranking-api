package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.repository.LeaderboardRepository
import fr.lfavreli.ranking.repository.TournamentRepository
import io.ktor.server.plugins.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun deleteTournamentPlayersHandler(tournamentId: String, dynamoDbClient: DynamoDbClient) {
    // 1. Ensure the tournament exists
    ensureTournamentExists(tournamentId, dynamoDbClient)

    // 2. Delete all players from the tournament leaderboard
    LeaderboardRepository.deleteTournamentPlayers(tournamentId, dynamoDbClient)
}

private fun ensureTournamentExists(tournamentId: String, dynamoDbClient: DynamoDbClient) {
    if (!TournamentRepository.isTournamentExists(tournamentId, dynamoDbClient)) {
        throw NotFoundException("Tournament not found")
    }
}