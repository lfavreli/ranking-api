package fr.lfavreli.ranking.features.players

import fr.lfavreli.ranking.features.players.model.CreatePlayerRequest
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

data class ErrorResponse(val message: String)

fun Route.PlayerRoutes(dynamoDbClient: DynamoDbClient) {

    route("/players") {
        // GET /players - List Players
        get {
            call.respondText("List Players")
        }

        // POST /players - Create Player
        post {
            val request = call.receive<CreatePlayerRequest>()
            val player = createPlayerHandler(request, dynamoDbClient)
            call.respond(player)
        }

        // Specific player routes
        route("/{playerId}") {
            // GET /players/{playerId} - Retrieve Player
            get {
                val playerId = call.parameters["playerId"] ?: throw BadRequestException("Missing or invalid playerId")
                val player = getPlayerHandler(playerId, dynamoDbClient)
                when {
                    player != null -> call.respond(player)
                    else -> throw NotFoundException("Player not found")
                }
            }

            // PUT /players/{playerId} - Update Player
            put {
                val playerId = call.parameters["playerId"]
                call.respondText("Update Player: $playerId")
            }
        }
    }
}
