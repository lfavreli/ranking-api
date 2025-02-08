package fr.lfavreli.ranking

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val dynamoDBService: DynamoDBService by inject()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/addItem") {
            val request = call.receive<ItemRequest>()
            dynamoDBService.putItem("Player", request.id, request.data)
            call.respondText("Élément ajouté !")
        }
    }
}
