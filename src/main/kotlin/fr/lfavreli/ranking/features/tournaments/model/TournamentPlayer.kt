package fr.lfavreli.ranking.features.tournaments.model

import kotlinx.serialization.Serializable

@Serializable
data class TournamentPlayer(
    val playerId: String,
    val displayName: String,
    val score: Int,
    val rank: Int
)