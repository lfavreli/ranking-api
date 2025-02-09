package fr.lfavreli.ranking

import fr.lfavreli.ranking.features.dynamodb.DynamoDBRoutes
import fr.lfavreli.ranking.features.players.PlayerRoutes
import fr.lfavreli.ranking.features.tournaments.TournamentRoutes
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun Application.configureRouting() {

    val dynamoDbClient : DynamoDbClient by inject()

    routing {

        get("/") {
            call.respondText("UP")
        }

        route("/api/ranking/v1") {
            swaggerUI(path = "/swagger-ui.html", swaggerFile = "specs/ranking-api.openapi.yml")
            DynamoDBRoutes(dynamoDbClient)
            PlayerRoutes(dynamoDbClient)
            TournamentRoutes()
        }
    }
}
