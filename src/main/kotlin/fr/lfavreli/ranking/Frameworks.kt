package fr.lfavreli.ranking

import fr.lfavreli.ranking.di.dynamoDBModule
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
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
}
