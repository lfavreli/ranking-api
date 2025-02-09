package fr.lfavreli.ranking.features.players.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlayerRequest(
    val displayName: String
)
