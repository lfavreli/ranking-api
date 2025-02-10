package fr.lfavreli.ranking.routes

import fr.lfavreli.ranking.exception.NotImplementedException
import fr.lfavreli.ranking.features.tournaments.createTournamentHandler
import fr.lfavreli.ranking.features.tournaments.getAllTournamentsHandler
import fr.lfavreli.ranking.features.tournaments.getTournamentByIdHandler
import fr.lfavreli.ranking.features.tournaments.model.CreateTournamentRequest
import fr.lfavreli.ranking.features.tournaments.model.UpdateScoreRequest
import fr.lfavreli.ranking.features.tournaments.updatePlayerScoreHandler
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun Route.TournamentRoutes(dynamoDbClient: DynamoDbClient) {
    route("/tournaments") {
        // GET /tournaments - List Tournaments
        get {
            val tournaments = getAllTournamentsHandler(dynamoDbClient)
            call.respond(mapOf("items" to tournaments))
        }

        // POST /tournaments - Create Tournament
        post {
            val request = call.receive<CreateTournamentRequest>()
            val tournament = createTournamentHandler(request, dynamoDbClient)
            call.respond(tournament)
        }

        // Specific Tournament routes
        route("/{tournamentId}") {
            // GET /tournaments/{tournamentId} - Retrieve Tournament
            get {
                val tournamentId = call.parameters["tournamentId"] ?: throw BadRequestException("Missing or invalid tournamentId")
                val tournament = getTournamentByIdHandler(tournamentId, dynamoDbClient)
                when {
                    tournament != null -> call.respond(tournament)
                    else -> throw NotFoundException("Tournament not found")
                }
            }

            delete {
                throw NotImplementedException("Deleting a Tournament is not yet implemented")
            }

            route("/players") {

                route("/{playerId}") {
                    // PATCH tournaments/{tournamentId}/players/{playerId}/score
                    patch("score") {
                        val tournamentId = call.parameters["tournamentId"] ?: throw BadRequestException("Missing or invalid tournamentId")
                        val playerId = call.parameters["playerId"] ?: throw BadRequestException("Missing or invalid playerId")

                        val request = call.receive<UpdateScoreRequest>()
                        val updatedTournamentPlayer = updatePlayerScoreHandler(tournamentId, playerId, request.newScore, dynamoDbClient)

                        when {
                            updatedTournamentPlayer != null -> call.respond(updatedTournamentPlayer)
                            else -> throw NotFoundException("Player not found!")
                        }
                    }
                }
            }
        }
    }
}
