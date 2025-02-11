package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.repository.TournamentRepository
import io.ktor.server.plugins.*
import org.koin.core.annotation.Single

@Single
class TournamentValidator(private val tournamentRepository: TournamentRepository) {

    fun ensureTournamentExists(tournamentId: String) {
        if (!tournamentRepository.isTournamentExists(tournamentId)) {
            throw NotFoundException("Tournament not found")
        }
    }
}
