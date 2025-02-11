package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.tournaments.model.LeaderboardEntry
import fr.lfavreli.ranking.repository.LeaderboardRepository
import org.koin.core.annotation.Single

@Single
class GetLeaderboardHandler(
    private val tournamentValidator: TournamentValidator,
    private val leaderboardRepository: LeaderboardRepository) {

    fun handle(tournamentId: String): List<LeaderboardEntry> {
        // 1. Ensure the tournament exists
        tournamentValidator.ensureTournamentExists(tournamentId)

        // 2. Get leaderboard entries
        return leaderboardRepository.getLeaderboard(tournamentId)
    }
}
