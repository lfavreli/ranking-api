package fr.lfavreli.ranking.features.tournaments.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateTournamentRequest(
    val description: String,
    val order: Order
)
