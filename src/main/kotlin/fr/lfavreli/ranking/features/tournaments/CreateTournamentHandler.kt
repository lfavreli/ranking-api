package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.dynamodb.TOURNAMENT_TABLE
import fr.lfavreli.ranking.features.tournaments.model.CreateTournamentRequest
import fr.lfavreli.ranking.features.tournaments.model.Tournament
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import java.time.OffsetDateTime
import java.util.*

private const val NO_PLAYERS = 0

fun createTournamentHandler(request: CreateTournamentRequest, dynamoDbClient: DynamoDbClient): Tournament {
    val tournamentId = UUID.randomUUID().toString()
    val lastUpdated = OffsetDateTime.now().toString()

    val tournament = Tournament(
        tournamentId = tournamentId,
        description = request.description,
        order = request.order,
        numPlayers = NO_PLAYERS,
        lastUpdated = lastUpdated
    )

    // Create insert request
    val itemRequest = PutItemRequest.builder()
        .tableName(TOURNAMENT_TABLE)
        .item(Tournament.toDynamoDbItem(tournament))
        .build()

    // Save to DynamoDB
    dynamoDbClient.putItem(itemRequest)
    // TODO: handle exceptions

    return tournament
}
