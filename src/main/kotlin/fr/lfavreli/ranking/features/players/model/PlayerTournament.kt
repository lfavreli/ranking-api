package fr.lfavreli.ranking.features.players.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerTournament(
    val tournamentId: String,
    val score: Int,
    val rank: Int
)
