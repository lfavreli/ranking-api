package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.tournaments.model.TournamentPlayer
import fr.lfavreli.ranking.repository.LeaderboardRepository
import fr.lfavreli.ranking.repository.PlayerRepository
import org.koin.core.annotation.Single

@Single
class UpdateScoreHandler(
    private val tournamentValidator: TournamentValidator,
    private val leaderboardRepository: LeaderboardRepository,
    private val playerRepository: PlayerRepository
) {

    fun handle(tournamentId: String, playerId: String, newScore: Int): TournamentPlayer {
        // 1. Ensure the Tournament exists
        tournamentValidator.ensureTournamentExists(tournamentId)

        // 2. Get current player data
        val player = playerRepository.getById(playerId)

        // 3. Update (or insert) the new score in Leaderboard
        leaderboardRepository.saveScore(tournamentId, player, newScore)

        // 4. Recalculate rank
        val rank = leaderboardRepository.getPlayerRank(tournamentId, newScore)

        // 5. Return the updated TournamentPlayer object
        return TournamentPlayer(
            playerId = playerId,
            displayName = player.displayName,
            score = newScore,
            rank = rank
        )
    }
}
