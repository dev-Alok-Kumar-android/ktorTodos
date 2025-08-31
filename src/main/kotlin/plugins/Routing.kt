package com.alokkumar.plugins

import com.alokkumar.auth.JwtService
import com.alokkumar.auth.hashPassword
import com.alokkumar.data.dto.TodoRequest
import com.alokkumar.data.dto.UserCredential
import com.alokkumar.data.dto.UserRegisterRequest
import com.alokkumar.repository.TodoRepository
import com.alokkumar.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val userRepository = UserRepository()
    val todoRepository = TodoRepository()
    val jwtService = JwtService()

    routing {
        // Public routes
        post("/register") {
            val request = call.receive<UserRegisterRequest>()
            val hashedPassword = hashPassword(request.password)
            val existing = userRepository.findUserByEmail(request.email) // Check email already exists
            if (existing != null) {
                call.respond(
                    HttpStatusCode.Conflict,
                    mapOf("error" to "Email already in use")
                )
                return@post
            }

            // ✅ safe to add user
            val addedUser = userRepository.addUser(
                UserRegisterRequest(
                    name = request.name,
                    email = request.email,
                    password = hashedPassword
                )
            )
            call.respond(
                HttpStatusCode.Created,
                addedUser
            )
        }

        post("/login") {
            val userCredentials = call.receive<UserCredential>()
            val user = userRepository.findUserByEmail(userCredentials.email)

            if (user != null && user.password == hashPassword(userCredentials.password)) {
                val token = jwtService.generateToken(user)
                call.respond(mapOf("token" to token))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }

        // Protected routes (require a valid JWT)
        authenticate("auth-jwt") {

            get("/todos") {
                val principal = call.principal<JWTPrincipal>()
                val authenticatedUserId = principal?.payload?.getClaim("userId")?.asInt()

                if (authenticatedUserId != null) {
                    val todos = todoRepository.readAllTodos(authenticatedUserId)
                    call.respond(todos)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }

            get("/todos/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val authenticatedUserId = principal?.payload?.getClaim("userId")?.asInt()

                if (authenticatedUserId != null) {
                    val todoId = call.parameters["id"]?.toIntOrNull()
                    if (todoId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid todo ID")
                        return@get
                    }
                    val todo = todoRepository.readTodoById(todoId, authenticatedUserId)
                    if (todo != null) {
                        call.respond(todo)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Todo not found or does not belong to user")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }

            post("/todos") {
                val principal = call.principal<JWTPrincipal>()
                val authenticatedUserId = principal?.payload?.getClaim("userId")?.asInt()

                if (authenticatedUserId != null) {
                    val todoRequest = call.receive<TodoRequest>()
                    val newTodo = todoRepository.addTodo(
                        TodoRequest(
                            userId = authenticatedUserId,
                            title = todoRequest.title,
                            description = todoRequest.description,
                            completed = todoRequest.completed
                        )
                    )
                    call.respond(HttpStatusCode.Created, newTodo)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "User ID not found in token.")
                }
            }


            put("/todos/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val authenticatedUserId = principal?.payload?.getClaim("userId")?.asInt()

                if (authenticatedUserId != null) {
                    val id = call.parameters["id"]?.toIntOrNull()
                    val todoRequest = call.receive<TodoRequest>()

                    if (id == null) {
                        call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                        return@put
                    }

                    // Pass the authenticated user's ID to the update function
                    val updatedTodo = todoRepository.updateTodo(id, todoRequest, authenticatedUserId)

                    if (updatedTodo != null) {
                        call.respond(updatedTodo)
                    } else {
                        call.respondText("Todo not found or does not belong to user", status = HttpStatusCode.NotFound)
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }

            delete("/todos/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val authenticatedUserId = principal?.payload?.getClaim("userId")?.asInt()

                if (authenticatedUserId != null) {
                    val todoId = call.parameters["id"]?.toIntOrNull()
                    if (todoId == null) {
                        call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                        return@delete
                    }
                    val deleted = todoRepository.deleteTodo(todoId, authenticatedUserId)
                    if (deleted) {
                        call.respondText("Todo deleted successfully", status = HttpStatusCode.OK)
                    } else {
                        call.respondText("Todo not found or does not belong to user", status = HttpStatusCode.NotFound)
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }

        }
    }
}