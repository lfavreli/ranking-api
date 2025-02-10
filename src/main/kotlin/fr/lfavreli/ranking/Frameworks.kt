package fr.lfavreli.ranking

import fr.lfavreli.ranking.di.dynamoDBModule
import fr.lfavreli.ranking.exception.NotImplementedException
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(dynamoDBModule)
    }

    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        handleException<BadRequestException>(HttpStatusCode.BadRequest)
        handleException<NotFoundException>(HttpStatusCode.NotFound)
        handleException<NotImplementedException>(HttpStatusCode.NotImplemented)
    }
}

inline fun <reified T : Throwable> StatusPagesConfig.handleException(statusCode: HttpStatusCode) {
    exception<T> { call, cause ->
        call.respond(statusCode, mapOf("message" to cause.message))
    }
}



