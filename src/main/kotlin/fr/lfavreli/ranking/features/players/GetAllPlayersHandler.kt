package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.repository.PlayerRepository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun getAllPlayersHandler(dynamoDbClient: DynamoDbClient): List<Player> {
    // 1. Retrieve all Players
    val players = PlayerRepository.fetchAll(dynamoDbClient)

    // 2. Map each player to retrieve their tournament data
    return players.map { player ->
        val tournaments = PlayerRepository.getParticipatingTournaments(player.playerId, dynamoDbClient)
        player.copy(tournaments = tournaments)
    }
}
