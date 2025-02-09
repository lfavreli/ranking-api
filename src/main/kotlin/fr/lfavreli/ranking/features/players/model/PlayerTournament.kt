package fr.lfavreli.ranking.features.players.model

import kotlinx.serialization.Serializable
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Serializable
data class PlayerTournament(
    val tournamentId: String,
    val score: Int,
    val rank: Int
) {
    companion object {
        fun fromDynamoDbItem(attribute: AttributeValue): PlayerTournament? {
            val m = attribute.m() ?: return null
            return PlayerTournament(
                tournamentId = m["tournamentId"]?.s() ?: return null,
                score = m["score"]?.n()?.toInt() ?: return null,
                rank = m["rank"]?.n()?.toInt() ?: return null
            )
        }

        fun toDynamoDbItem(tournament: PlayerTournament): Map<String, AttributeValue> {
            return mapOf(
                "tournamentId" to AttributeValue.builder().s(tournament.tournamentId).build(),
                "score" to AttributeValue.builder().n(tournament.score.toString()).build(),
                "rank" to AttributeValue.builder().n(tournament.rank.toString()).build()
            )
        }
    }
}
