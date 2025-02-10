package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.dynamodb.TOURNAMENT_TABLE
import fr.lfavreli.ranking.features.tournaments.model.Tournament
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.ScanRequest

fun getAllTournamentsHandler(dynamoDbClient: DynamoDbClient): List<Tournament> {
    // Create query request
    val request = ScanRequest.builder()
        .tableName(TOURNAMENT_TABLE)
        .build()

    // Run query request
    val result = dynamoDbClient.scan(request)
    // TODO: handle exceptions

    return result.items().mapNotNull { Tournament.fromDynamoDbItem(it) }
}