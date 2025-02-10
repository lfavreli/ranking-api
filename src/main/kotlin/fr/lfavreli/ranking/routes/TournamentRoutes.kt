package fr.lfavreli.ranking.routes

import fr.lfavreli.ranking.exception.NotImplementedException
import fr.lfavreli.ranking.features.tournaments.*
import fr.lfavreli.ranking.features.tournaments.model.CreateTournamentRequest
import fr.lfavreli.ranking.features.tournaments.model.TournamentLeaderboardResponse
import fr.lfavreli.ranking.features.tournaments.model.UpdateScoreRequest
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
                call.respond(tournament)
            }

            // DELETE /tournaments/{tournamentId} - Delete Tournament
            delete {
                throw NotImplementedException("Deleting a Tournament is not yet implemented")
            }

            route("/leaderboard") {
                // GET tournaments/{tournamentId}/leaderboard - Retrieve Leaderboard
                get {
                    val tournamentId = call.parameters["tournamentId"] ?: throw BadRequestException("Missing or invalid tournamentId")
                    val leaderboard = getLeaderboardHandler(tournamentId, dynamoDbClient)
                    call.respond(TournamentLeaderboardResponse(tournamentId, leaderboard))
                }
            }

            route("/players") {
                // DELETE tournaments/{tournamentId}/players - Delete Tournament Players
                delete {
                    val tournamentId = call.parameters["tournamentId"] ?: throw BadRequestException("Missing or invalid tournamentId")
                    deleteTournamentPlayersHandler(tournamentId, dynamoDbClient)
                    call.respond(mapOf(
                        "tournamentId" to tournamentId,
                        "message" to "Players successfully removed from tournament."
                    ))
                }

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
