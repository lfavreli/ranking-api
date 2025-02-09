package fr.lfavreli.ranking.features.dynamodb

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*

suspend fun createTablesHandler(call: RoutingCall, client: DynamoDbClient) {
    try {
        val createTournamentTableRequest = createTournamentTableRequest()
        client.createTable(createTournamentTableRequest)

        val createPlayerTableRequest = createPlayerTableRequest()
        client.createTable(createPlayerTableRequest)

        val createLeaderboardTableRequest = createLeaderboardTableRequest()
        client.createTable(createLeaderboardTableRequest)

        call.respondText("The database has been initialised!")
    } catch (e: Exception) {
        println("Error creating table: ${e.message}")
        call.respondText("Failed to initialise the database", status = HttpStatusCode.InternalServerError)
    }
}

private fun createTournamentTableRequest(): CreateTableRequest? {
    return CreateTableRequest.builder()
        .tableName(TOURNAMENT_TABLE)
        .keySchema(KeySchemaElement.builder().attributeName(TOURNAMENT_ID).keyType(KeyType.HASH).build())
        .attributeDefinitions(AttributeDefinition.builder().attributeName(TOURNAMENT_ID).attributeType(ScalarAttributeType.S).build())
        .billingMode(BillingMode.PAY_PER_REQUEST)
        .build()
}

private fun createPlayerTableRequest(): CreateTableRequest? {
    return CreateTableRequest.builder()
        .tableName(PLAYER_TABLE)
        .keySchema(KeySchemaElement.builder().attributeName(PLAYER_ID).keyType(KeyType.HASH).build())
        .attributeDefinitions(AttributeDefinition.builder().attributeName(PLAYER_ID).attributeType(ScalarAttributeType.S).build())
        .billingMode(BillingMode.PAY_PER_REQUEST)
        .build()
}

private fun createLeaderboardTableRequest(): CreateTableRequest? {
    return CreateTableRequest.builder()
        .tableName(LEADERBOARD_TABLE)
        .keySchema(
            listOf(
                KeySchemaElement.builder().attributeName(TOURNAMENT_ID).keyType(KeyType.HASH).build(),
                KeySchemaElement.builder().attributeName(LEADERBOARD_SORT_KEY).keyType(KeyType.RANGE).build()
            )
        )
        .attributeDefinitions(
            listOf(
                AttributeDefinition.builder().attributeName(TOURNAMENT_ID).attributeType(ScalarAttributeType.S).build(),
                AttributeDefinition.builder().attributeName(LEADERBOARD_SORT_KEY).attributeType(ScalarAttributeType.N).build()
            )
        )
        .billingMode(BillingMode.PAY_PER_REQUEST)
        .build()
}
