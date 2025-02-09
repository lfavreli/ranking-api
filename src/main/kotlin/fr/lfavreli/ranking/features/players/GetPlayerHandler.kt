package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.dynamodb.PLAYER_ID
import fr.lfavreli.ranking.features.dynamodb.PLAYER_TABLE
import fr.lfavreli.ranking.features.players.model.Player
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest

fun getPlayerHandler(playerId: String, dynamoDbClient: DynamoDbClient) : Player? {
    val request = GetItemRequest.builder()
        .tableName(PLAYER_TABLE)
        .key(mapOf(PLAYER_ID to AttributeValue.builder().s(playerId).build()))
        .build()

    val result = dynamoDbClient.getItem(request)

    return Player.fromDynamoDbItem(result.item())
}
