package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.dynamodb.PLAYER_TABLE
import fr.lfavreli.ranking.features.players.model.CreatePlayerRequest
import fr.lfavreli.ranking.features.players.model.Player
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import java.time.OffsetDateTime
import java.util.*

fun createPlayerHandler(request: CreatePlayerRequest, dynamoDbClient: DynamoDbClient) : Player {

    val playerId = UUID.randomUUID().toString()
    val lastUpdated = OffsetDateTime.now().toString()

    val player = Player(
        playerId = playerId,
        displayName = request.displayName,
        tournaments = emptyList(),
        lastUpdated = lastUpdated
    )

    // Convert to DynamoDB item
    val item = Player.toDynamoDbItem(player)

    // Create insert request
    val itemRequest = PutItemRequest.builder()
        .tableName(PLAYER_TABLE)
        .item(item)
        .build()

    // Save to DynamoDB
    dynamoDbClient.putItem(itemRequest)
    // TODO: handle exceptions

    return player;
}
