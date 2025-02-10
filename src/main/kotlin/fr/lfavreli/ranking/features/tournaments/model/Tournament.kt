package fr.lfavreli.ranking.features.tournaments.model

import kotlinx.serialization.Serializable
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Serializable
data class Tournament(
    val tournamentId: String,
    val description: String,
    val order: Order,
    val numPlayers: Int?,
    val lastUpdated: String
) {
    companion object {
        fun fromDynamoDbItem(item: Map<String, AttributeValue>): Tournament? {
            return Tournament(
                tournamentId = item["tournamentId"]?.s() ?: return null,
                description = item["description"]?.s() ?: return null,
                order = item["order"]?.s()?.let { Order.entries.find { enum -> enum.value == it } } ?: return null,
                numPlayers = null,
                lastUpdated = item["lastUpdated"]?.s() ?: return null
            )
        }

        fun toDynamoDbItem(tournament: Tournament): Map<String, AttributeValue> {
            return mapOf(
                "tournamentId" to AttributeValue.builder().s(tournament.tournamentId).build(),
                "description" to AttributeValue.builder().s(tournament.description).build(),
                "order" to AttributeValue.builder().s(tournament.order.value).build(),
                "lastUpdated" to AttributeValue.builder().s(tournament.lastUpdated).build()
            )
        }
    }
}
