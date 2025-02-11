package fr.lfavreli.ranking

import fr.lfavreli.ranking.routes.DynamoDBRoutes
import fr.lfavreli.ranking.routes.PlayerRoutes
import fr.lfavreli.ranking.routes.TournamentRoutes
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val dynamoDBRoutes by inject<DynamoDBRoutes>()
    val playerRoutes by inject<PlayerRoutes>()
    val tournamentRoutes by inject<TournamentRoutes>()

    routing {

        get("/") {
            call.respondText("UP")
        }

        route("/api/ranking/v1") {
            swaggerUI(path = "/swagger-ui.html", swaggerFile = "specs/ranking-api.openapi.yml")
            with(dynamoDBRoutes) { registerRoutes() }
            with(playerRoutes) { registerRoutes() }
            with(tournamentRoutes) { registerRoutes() }
        }
    }
}
