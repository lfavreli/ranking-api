package fr.lfavreli.ranking.features.players.model

import kotlinx.serialization.Serializable
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Serializable
data class Player(
    val playerId: String,
    val displayName: String,
    val tournaments: List<PlayerTournament>,
    val lastUpdated: String
)

fun Player.toDynamoDbItem(): Map<String, AttributeValue> {
    return mapOf(
        "playerId" to AttributeValue.builder().s(this.playerId).build(),
        "displayName" to AttributeValue.builder().s(this.displayName).build(),
        "tournaments" to AttributeValue.builder().l(toTournamentsAttributeValues()).build(),
        "lastUpdated" to AttributeValue.builder().s(this.lastUpdated).build()
    )
}

private fun Player.toTournamentsAttributeValues(): List<AttributeValue> {
    return this.tournaments.map { tournament ->
        AttributeValue.builder()
            .m(tournament.toDynamoDbItem())
            .build()
    }
}
