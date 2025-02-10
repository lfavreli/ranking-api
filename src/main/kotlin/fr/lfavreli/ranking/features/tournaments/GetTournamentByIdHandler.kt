package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.dynamodb.TOURNAMENT_ID
import fr.lfavreli.ranking.features.dynamodb.TOURNAMENT_TABLE
import fr.lfavreli.ranking.features.tournaments.model.Tournament
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest

fun getTournamentByIdHandler(tournamentId: String, dynamoDbClient: DynamoDbClient) : Tournament? {
    // Create query request
    val request = GetItemRequest.builder()
        .tableName(TOURNAMENT_TABLE)
        .key(mapOf(TOURNAMENT_ID to AttributeValue.builder().s(tournamentId).build()))
        .build()

    // Run query request
    val result = dynamoDbClient.getItem(request)
    // TODO: handle exceptions

    return Tournament.fromDynamoDbItem(result.item())
}
