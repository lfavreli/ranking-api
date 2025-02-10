package fr.lfavreli.ranking.features.tournaments.model

import kotlinx.serialization.Serializable
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Serializable
data class TournamentPlayer(
    val playerId: String,
    val displayName: String,
    val score: Int,
    val rank: Int
) {
    companion object {
        fun fromDynamoDbItem(item: Map<String, AttributeValue>): TournamentPlayer? {
            return TournamentPlayer(
                playerId = item["playerId"]?.s() ?: return null,
                displayName = item["displayName"]?.s() ?: return null,
                score = item["score"]?.n()?.toInt() ?: return null,
                rank = item["rank"]?.n()?.toInt() ?: return null
            )
        }

        fun toDynamoDbItem(player: TournamentPlayer): Map<String, AttributeValue> {
            return mapOf(
                "playerId" to AttributeValue.builder().s(player.playerId).build(),
                "displayName" to AttributeValue.builder().s(player.displayName).build(),
                "score" to AttributeValue.builder().n(player.score.toString()).build(),
                "rank" to AttributeValue.builder().n(player.rank.toString()).build()
            )
        }
    }
}