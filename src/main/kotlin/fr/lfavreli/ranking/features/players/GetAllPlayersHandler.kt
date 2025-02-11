package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.repository.PlayerRepository
import org.koin.core.annotation.Single

@Single
class GetAllPlayersHandler(private val playerRepository: PlayerRepository) {

    fun handle(): List<Player> {
        // 1. Retrieve all Players
        val players = playerRepository.fetchAll()

        // 2. Map each player to retrieve their tournament data
        return players.map { player ->
            val tournaments = playerRepository.getParticipatingTournaments(player.playerId)
            player.copy(tournaments = tournaments)
        }
    }
}
