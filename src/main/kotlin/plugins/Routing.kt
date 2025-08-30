package com.alokkumar.plugins

import com.alokkumar.data.Todo
import com.alokkumar.repository.TodoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val repository = TodoRepository()

    routing {
        post("/login") {
            // ... validate user, generate JWT, and send it back
        }

        // GET all todos
        get("/todos") {
            val todos = repository.getAllTodos()
            call.respond(todos)
        }

        // GET a single todo by ID
        get("/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                return@get
            }
            val todo = repository.getTodo(id)
            if (todo != null) {
                call.respond(todo)
            } else {
                call.respondText("Todo not found", status = HttpStatusCode.NotFound)
            }
        }

        // CREATE a new todo
        post("/todos") {
            val todo = call.receive<Todo>()
            val newTodo = repository.addTodo(todo)
            call.respond(newTodo)
        }

        // UPDATE an existing todo
        put("/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                return@put
            }
            val todo = call.receive<Todo>()
            val updatedTodo = repository.updateTodo(id, todo)
            if (updatedTodo != null) {
                call.respond(updatedTodo)
            } else {
                call.respondText("Todo not found", status = HttpStatusCode.NotFound)
            }
        }

        // DELETE a todo
        delete("/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                return@delete
            }
            val deleted = repository.deleteTodo(id)
            if (deleted) {
                call.respondText("Todo deleted successfully", status = HttpStatusCode.OK)
            } else {
                call.respondText("Todo not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}


