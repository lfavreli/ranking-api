package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.dynamodb.PLAYER_TABLE
import fr.lfavreli.ranking.features.players.model.CreatePlayerRequest
import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.repository.PlayerRepository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import java.time.OffsetDateTime
import java.util.*

fun createPlayerHandler(request: CreatePlayerRequest, dynamoDbClient: DynamoDbClient) : Player {
    // 1. Generate player details
    val player = generatePlayerEntity(request)

    // 2. Save the player in the database
    PlayerRepository.save(player, dynamoDbClient)

    return player;
}

private fun generatePlayerEntity(request: CreatePlayerRequest): Player {
    val playerId = UUID.randomUUID().toString()
    val lastUpdated = OffsetDateTime.now().toString()

    return Player(
        playerId = playerId,
        displayName = request.displayName,
        tournaments = emptyList(),
        lastUpdated = lastUpdated
    )
}
