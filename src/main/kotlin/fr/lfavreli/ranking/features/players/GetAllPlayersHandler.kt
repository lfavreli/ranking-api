package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.dynamodb.PLAYER_TABLE
import fr.lfavreli.ranking.features.players.model.Player
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.ScanRequest

fun getAllPlayersHandler(dynamoDbClient: DynamoDbClient): List<Player> {
    val request = ScanRequest.builder()
        .tableName(PLAYER_TABLE)
        .build()

    val result = dynamoDbClient.scan(request)
    return result.items().mapNotNull { Player.fromDynamoDbItem(it) }
}