package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.tournaments.model.LeaderboardEntry
import fr.lfavreli.ranking.repository.LeaderboardRepository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun getLeaderboardHandler(tournamentId: String, dynamoDbClient: DynamoDbClient): List<LeaderboardEntry> {
    // 1. Ensure the tournament exists
    TournamentUtils.ensureTournamentExists(tournamentId, dynamoDbClient)

    // 2. Get leaderboard entries
    return LeaderboardRepository.getLeaderboard(tournamentId, dynamoDbClient)
}
