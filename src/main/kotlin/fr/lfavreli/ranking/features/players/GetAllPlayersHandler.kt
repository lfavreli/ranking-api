package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.dynamodb.PLAYER_TABLE
import fr.lfavreli.ranking.features.players.model.Player
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.ScanRequest

fun getAllPlayersHandler(dynamoDbClient: DynamoDbClient): List<Player> {
    // 1. Create scan request to retrieve all players
    val request = ScanRequest.builder()
        .tableName(PLAYER_TABLE)
        .build()

    val result = dynamoDbClient.scan(request)

    // 2. Map each player to retrieve their tournament data
    return result.items().mapNotNull {
        val player = Player.fromDynamoDbItem(it)
        player?.let {
            val tournaments = getPlayerTournamentsFromLeaderboard(player.playerId, dynamoDbClient)
            player.copy(tournaments = tournaments)
        }
    }
}
