package fr.lfavreli.ranking.features.tournaments.model

import kotlinx.serialization.Serializable
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Serializable
data class LeaderboardEntry(
    val playerId: String,
    val displayName: String,
    val score: Int,
    val rank: Int
) {
    companion object {
        fun fromDynamoDbItem(item: Map<String, AttributeValue>, rank: Int): LeaderboardEntry? {
            return LeaderboardEntry(
                playerId = item["playerId"]?.s() ?: return null,
                displayName = item["displayName"]?.s() ?: "Unknown",
                score = item["score"]?.n()?.toInt() ?: return null,
                rank = rank
            )
        }
    }
}
