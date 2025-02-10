package fr.lfavreli.ranking.features.tournaments.model

import kotlinx.serialization.Serializable

@Serializable
data class TournamentLeaderboardResponse(
    val tournamentId: String,
    val leaderboard: List<LeaderboardEntry>
)
