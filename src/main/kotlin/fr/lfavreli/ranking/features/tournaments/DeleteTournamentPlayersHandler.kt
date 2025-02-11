package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.repository.LeaderboardRepository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun deleteTournamentPlayersHandler(tournamentId: String, dynamoDbClient: DynamoDbClient) {
    // 1. Ensure the tournament exists
    TournamentUtils.ensureTournamentExists(tournamentId, dynamoDbClient)

    // 2. Delete all players from the tournament leaderboard
    LeaderboardRepository.deleteTournamentPlayers(tournamentId, dynamoDbClient)
}
