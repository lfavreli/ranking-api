package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.tournaments.model.Tournament
import fr.lfavreli.ranking.repository.TournamentRepository
import org.koin.core.annotation.Single

@Single
class GetAllTournamentsHandler(private val tournamentRepository: TournamentRepository) {

    fun handle(): List<Tournament> {
        // 1. Retrieve all Tournaments
        return tournamentRepository.fetchAll()
    }
}
