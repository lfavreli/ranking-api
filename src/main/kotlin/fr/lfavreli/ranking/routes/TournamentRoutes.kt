package fr.lfavreli.ranking.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.TournamentRoutes() {
    route("/tournaments") {
        // GET /tournaments - List Tournaments
        get {
            call.respondText("List all tournaments")
        }

        // POST /tournaments - Create Tournament
        post {
            call.respondText("Create new tournament", status = HttpStatusCode.Created)
        }
    }
}
