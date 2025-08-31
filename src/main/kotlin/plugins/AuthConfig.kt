package com.alokkumar.plugins

import com.alokkumar.auth.JwtService
import com.alokkumar.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuthentication() {
    val jwtService = JwtService()
    val userRepository = UserRepository()

    install(Authentication) {
        jwt("auth-jwt") {
            realm = "Ktor Todo App"
            verifier(jwtService.verifier)
            validate { credential ->
                val id = credential.payload.getClaim("userId").asInt()
                val user = userRepository.findUserById(id)
                if (user != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}