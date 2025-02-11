package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.tournaments.model.TournamentPlayer
import fr.lfavreli.ranking.repository.LeaderboardRepository
import fr.lfavreli.ranking.repository.PlayerRepository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun updatePlayerScoreHandler(tournamentId: String, playerId: String, newScore: Int, dynamoDbClient: DynamoDbClient): TournamentPlayer? {
    // 1. Ensure the Tournament exists
    TournamentUtils.ensureTournamentExists(tournamentId, dynamoDbClient)

    // 2. Get current player data
    val player = PlayerRepository.getById(playerId, dynamoDbClient)

    // 3. Update (or insert) the new score in Leaderboard
    LeaderboardRepository.saveScore(tournamentId, player, newScore, dynamoDbClient)

    // 4. Recalculate rank
    val rank = LeaderboardRepository.getPlayerRank(tournamentId, newScore, dynamoDbClient)

    // 5. Return the updated TournamentPlayer object
    return TournamentPlayer(
        playerId = playerId,
        displayName = player.displayName,
        score = newScore,
        rank = rank
    )
}
