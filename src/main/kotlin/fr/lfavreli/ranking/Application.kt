package fr.lfavreli.ranking

import fr.lfavreli.ranking.configureRouting

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureFrameworks()
    configureRouting()
}
