package fr.lfavreli.ranking.routes

import fr.lfavreli.ranking.exception.NotImplementedException
import fr.lfavreli.ranking.features.players.CreatePlayerHandler
import fr.lfavreli.ranking.features.players.GetAllPlayersHandler
import fr.lfavreli.ranking.features.players.GetPlayerByIdHandler
import fr.lfavreli.ranking.features.players.model.CreatePlayerRequest
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.annotation.Single

@Single
class PlayerRoutes(
    private val getAllPlayersHandler: GetAllPlayersHandler,
    private val createPlayerHandler: CreatePlayerHandler,
    private val getPlayerByIdHandler: GetPlayerByIdHandler
) {

    fun Route.registerRoutes() {
        route("/players") {
            // GET /players - List Players
            get {
                val players = getAllPlayersHandler.handle()
                call.respond(mapOf("items" to players))
            }

            // POST /players - Create Player
            post {
                val request = call.receive<CreatePlayerRequest>()
                val player = createPlayerHandler.handle(request)
                call.respond(player)
            }

            // Specific Player routes
            route("/{playerId}") {
                // GET /players/{playerId} - Retrieve Player
                get {
                    val playerId = call.parameters["playerId"] ?: throw BadRequestException("Missing or invalid playerId")
                    val player = getPlayerByIdHandler.handle(playerId)
                    call.respond(player)
                }

                // PUT /players/{playerId} - Update Player
                put {
                    throw NotImplementedException("Updating a Player is not yet implemented")
                }
            }
        }
    }
}
