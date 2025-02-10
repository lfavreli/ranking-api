package fr.lfavreli.ranking.routes

import fr.lfavreli.ranking.features.tournaments.createTournamentHandler
import fr.lfavreli.ranking.features.tournaments.model.CreateTournamentRequest
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun Route.TournamentRoutes(dynamoDbClient: DynamoDbClient) {
    route("/tournaments") {
        // GET /tournaments - List Tournaments
        get {
            call.respondText("List all tournaments")
        }

        // POST /tournaments - Create Tournament
        post {
            val request = call.receive<CreateTournamentRequest>()
            val tournament = createTournamentHandler(request, dynamoDbClient)
            call.respond(tournament)
        }
    }
}
