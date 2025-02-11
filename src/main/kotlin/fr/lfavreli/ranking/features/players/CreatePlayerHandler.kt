package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.players.model.CreatePlayerRequest
import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.repository.PlayerRepository
import org.koin.core.annotation.Single
import java.time.OffsetDateTime
import java.util.*

@Single
class CreatePlayerHandler(private val playerRepository: PlayerRepository) {

    fun handle(request: CreatePlayerRequest) : Player {
        // 1. Generate player details
        val player = generatePlayerEntity(request)

        // 2. Save the player in the database
        playerRepository.save(player)

        return player
    }

    private fun generatePlayerEntity(request: CreatePlayerRequest): Player {
        val playerId = UUID.randomUUID().toString()
        val lastUpdated = OffsetDateTime.now().toString()

        return Player(
            playerId = playerId,
            displayName = request.displayName,
            tournaments = emptyList(),
            lastUpdated = lastUpdated
        )
    }
}
