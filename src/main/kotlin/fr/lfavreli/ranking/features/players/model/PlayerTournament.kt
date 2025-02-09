package fr.lfavreli.ranking.features.players.model

import kotlinx.serialization.Serializable
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Serializable
data class PlayerTournament(
    val tournamentId: String,
    val score: Int,
    val rank: Int
)

fun PlayerTournament.toDynamoDbItem(): Map<String, AttributeValue> {
    return mapOf(
        "tournamentId" to AttributeValue.builder().s(this.tournamentId).build(),
        "score" to AttributeValue.builder().n(this.score.toString()).build(),
        "rank" to AttributeValue.builder().n(this.rank.toString()).build()
    )
}
