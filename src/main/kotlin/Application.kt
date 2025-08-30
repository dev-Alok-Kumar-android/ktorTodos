package com.alokkumar

import com.alokkumar.plugins.configureAuthentication
import com.alokkumar.plugins.configureRouting
import com.alokkumar.plugins.configureSerialization
import com.alokkumar.repository.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.cio.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()

    configureAuthentication()
    configureSerialization()
    configureRouting()
}

