package fr.lfavreli.ranking.features.dynamodb

import fr.lfavreli.ranking.repository.DynamoDBRepository
import org.koin.core.annotation.Single
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest

@Single
class DeleteDatabaseHandler(private val dynamoDBRepository: DynamoDBRepository) {

    fun handle() {
        val deleteTournamentTableRequest = DeleteTableRequest.builder().tableName(TOURNAMENT_TABLE).build()
        dynamoDBRepository.deleteTable(deleteTournamentTableRequest)

        val deletePlayerTableRequest = DeleteTableRequest.builder().tableName(PLAYER_TABLE).build()
        dynamoDBRepository.deleteTable(deletePlayerTableRequest)

        val deleteLeaderboardTableRequest = DeleteTableRequest.builder().tableName(LEADERBOARD_TABLE).build()
        dynamoDBRepository.deleteTable(deleteLeaderboardTableRequest)
    }
}
