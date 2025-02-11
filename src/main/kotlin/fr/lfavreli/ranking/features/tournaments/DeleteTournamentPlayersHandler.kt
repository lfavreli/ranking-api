package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.repository.LeaderboardRepository
import org.koin.core.annotation.Single

@Single
class DeleteTournamentPlayersHandler(
    private val tournamentValidator: TournamentValidator,
    private val leaderboardRepository: LeaderboardRepository) {

    fun handle(tournamentId: String) {
        // 1. Ensure the tournament exists
        tournamentValidator.ensureTournamentExists(tournamentId)

        // 2. Delete all players from the tournament leaderboard
        leaderboardRepository.deleteTournamentPlayers(tournamentId)
    }
}
