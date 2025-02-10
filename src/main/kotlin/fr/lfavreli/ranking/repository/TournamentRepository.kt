package fr.lfavreli.ranking.repository

import fr.lfavreli.ranking.features.dynamodb.TOURNAMENT_ID
import fr.lfavreli.ranking.features.dynamodb.TOURNAMENT_TABLE
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

object TournamentRepository {

    fun isTournamentExists(tournamentId: String, dynamoDbClient: DynamoDbClient): Boolean {
        val tournamentItem = DynamoDBOperations.getItem(
            tableName = TOURNAMENT_TABLE,
            key = mapOf(TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build()),
            client = dynamoDbClient
        )
        return tournamentItem.isNotEmpty()
    }

}