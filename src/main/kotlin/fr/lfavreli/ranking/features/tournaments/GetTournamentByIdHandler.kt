package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.tournaments.model.Tournament
import fr.lfavreli.ranking.repository.TournamentRepository
import org.koin.core.annotation.Single

@Single
class GetTournamentByIdHandler(private val tournamentRepository: TournamentRepository) {

    fun handle(tournamentId: String) : Tournament {
        // 1. Retrieve player details from Player table
        return tournamentRepository.getById(tournamentId)
    }
}
