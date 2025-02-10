package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.players.model.Player
import fr.lfavreli.ranking.repository.PlayerRepository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun getPlayerByIdHandler(playerId: String, dynamoDbClient: DynamoDbClient): Player {
    // 1. Retrieve player details from Player table
    val player = PlayerRepository.getById(playerId, dynamoDbClient)

    // 2. Fetch related Tournaments
    val playerTournaments = PlayerRepository.getParticipatingTournaments(playerId, dynamoDbClient)

    // 3. Return player object with real-time rankings
    return player.copy(tournaments = playerTournaments)
}
