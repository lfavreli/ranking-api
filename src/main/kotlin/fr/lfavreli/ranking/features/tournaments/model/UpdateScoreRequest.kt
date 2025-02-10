package fr.lfavreli.ranking.features.tournaments.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateScoreRequest(
    val newScore: Int
)
