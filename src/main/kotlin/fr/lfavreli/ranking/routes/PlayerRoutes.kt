package fr.lfavreli.ranking.routes

import fr.lfavreli.ranking.exception.NotImplementedException
import fr.lfavreli.ranking.features.players.createPlayerHandler
import fr.lfavreli.ranking.features.players.getAllPlayersHandler
import fr.lfavreli.ranking.features.players.getPlayerByIdHandler
import fr.lfavreli.ranking.features.players.model.CreatePlayerRequest
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun Route.PlayerRoutes(dynamoDbClient: DynamoDbClient) {

    route("/players") {
        // GET /players - List Players
        get {
            val players = getAllPlayersHandler(dynamoDbClient)
            call.respond(mapOf("items" to players))
        }

        // POST /players - Create Player
        post {
            val request = call.receive<CreatePlayerRequest>()
            val player = createPlayerHandler(request, dynamoDbClient)
            call.respond(player)
        }

        // Specific Player routes
        route("/{playerId}") {
            // GET /players/{playerId} - Retrieve Player
            get {
                val playerId = call.parameters["playerId"] ?: throw BadRequestException("Missing or invalid playerId")
                val player = getPlayerByIdHandler(playerId, dynamoDbClient)
                call.respond(player)
            }

            // PUT /players/{playerId} - Update Player
            put {
                throw NotImplementedException("Updating a Player is not yet implemented")
            }
        }
    }
}
