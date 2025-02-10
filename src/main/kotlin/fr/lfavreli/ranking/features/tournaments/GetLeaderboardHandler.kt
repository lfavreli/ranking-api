package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.tournaments.model.LeaderboardEntry
import fr.lfavreli.ranking.repository.LeaderboardRepository
import fr.lfavreli.ranking.repository.TournamentRepository
import io.ktor.server.plugins.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun getLeaderboardHandler(tournamentId: String, dynamoDbClient: DynamoDbClient): List<LeaderboardEntry> {
    // 1. Ensure the tournament exists
    if (!TournamentRepository.isTournamentExists(tournamentId, dynamoDbClient)) {
        throw NotFoundException("Tournament not found")
    }
    val toto = LeaderboardRepository.getLeaderboard(tournamentId, dynamoDbClient)
    println(toto)

    // 2. Get leaderboard entries
    return toto
}
