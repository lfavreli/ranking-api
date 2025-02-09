package fr.lfavreli.ranking.features.players.model

import kotlinx.serialization.Serializable
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Serializable
data class Player(
    val playerId: String,
    val displayName: String,
    val tournaments: List<PlayerTournament>,
    val lastUpdated: String
)  {
    companion object {
        fun fromDynamoDbItem(item: Map<String, AttributeValue>): Player? {
            return Player(
                playerId = item["playerId"]?.s() ?: return null,
                displayName = item["displayName"]?.s() ?: return null,
                tournaments = item["tournaments"]?.l()?.mapNotNull { PlayerTournament.fromDynamoDbItem(it) } ?: emptyList(),
                lastUpdated = item["lastUpdated"]?.s() ?: return null
            )
        }

        fun toDynamoDbItem(player: Player): Map<String, AttributeValue> {
            return mapOf(
                "playerId" to AttributeValue.builder().s(player.playerId).build(),
                "displayName" to AttributeValue.builder().s(player.displayName).build(),
                "tournaments" to AttributeValue.builder().l(toAttributeValues(player.tournaments)).build(),
                "lastUpdated" to AttributeValue.builder().s(player.lastUpdated).build()
            )
        }

        private fun toAttributeValues(tournaments: List<PlayerTournament>): List<AttributeValue> {
            return tournaments.map {
                AttributeValue.builder().m(PlayerTournament.toDynamoDbItem(it)).build()
            }
        }
    }
}
