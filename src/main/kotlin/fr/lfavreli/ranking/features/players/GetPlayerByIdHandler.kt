package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.repository.PlayerRepository
import org.koin.core.annotation.Single

@Single
class GetPlayerByIdHandler(private val playerRepository: PlayerRepository) {

    fun handle(playerId: String): Player {
        // 1. Retrieve player details from Player table
        val player = playerRepository.getById(playerId)

        // 2. Fetch related Tournaments
        val playerTournaments = playerRepository.getParticipatingTournaments(playerId)

        // 3. Return player object with real-time rankings
        return player.copy(tournaments = playerTournaments)
    }
}
