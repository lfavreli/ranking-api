package fr.lfavreli.ranking.routes

import fr.lfavreli.ranking.features.dynamodb.CreateDatabaseHandler
import fr.lfavreli.ranking.features.dynamodb.DeleteDatabaseHandler
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.annotation.Single

@Single
class DynamoDBRoutes(
    private val createTablesHandler: CreateDatabaseHandler,
    private val deleteTablesHandler: DeleteDatabaseHandler) {

    fun Route.registerRoutes() {

        route("/db") {
            // GET /db - Setup database
            get {
                createTablesHandler.handle()
                call.respond(mapOf("message" to "The database has been initialised!"))
            }

            // DELETE /db - Delete database
            delete {
                deleteTablesHandler.handle()
                call.respond(mapOf("message" to "The database has been removed!"))
            }
        }
    }
}
