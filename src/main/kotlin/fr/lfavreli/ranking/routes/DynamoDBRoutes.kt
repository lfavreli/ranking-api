package fr.lfavreli.ranking.routes

import fr.lfavreli.ranking.features.dynamodb.createTablesHandler
import fr.lfavreli.ranking.features.dynamodb.deleteTablesHandler
import io.ktor.server.routing.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun Route.DynamoDBRoutes(dynamoDbClient: DynamoDbClient) {

    route("/db") {
        // GET /db - Setup database
        get { createTablesHandler(call, dynamoDbClient) }

        // DELETE /db - Delete database
        delete { deleteTablesHandler(call, dynamoDbClient) }
    }
}
