package fr.lfavreli.ranking.features.dynamodb

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest

suspend fun deleteTablesHandler(call: RoutingCall, client: DynamoDbClient) {
    try {
        val deleteTournamentTableRequest = DeleteTableRequest.builder().tableName(TOURNAMENT_TABLE).build()
        client.deleteTable(deleteTournamentTableRequest)

        val deletePlayerTableRequest = DeleteTableRequest.builder().tableName(PLAYER_TABLE).build()
        client.deleteTable(deletePlayerTableRequest)

        val deleteLeaderboardTableRequest = DeleteTableRequest.builder().tableName(LEADERBOARD_TABLE).build()
        client.deleteTable(deleteLeaderboardTableRequest)

        call.respondText("The database has been removed!")
    } catch (e: Exception) {
        println("Error deleting table: ${e.message}")
        call.respondText("Failed to removing the database", status = HttpStatusCode.InternalServerError)
    }
}
