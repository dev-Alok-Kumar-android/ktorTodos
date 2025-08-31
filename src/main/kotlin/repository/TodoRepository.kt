package com.alokkumar.repository

import com.alokkumar.data.dto.Todo
import com.alokkumar.data.dto.TodoRequest
import com.alokkumar.data.tables.TodoTable
import com.alokkumar.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TodoRepository {

    suspend fun readAllTodos(userId: Int): List<Todo> = dbQuery {
        TodoTable.selectAll().where { TodoTable.userId eq userId }
            .map { toTodo(it) }
    }

    suspend fun readTodoById(todoId: Int, userId: Int): Todo? = dbQuery {
        TodoTable.selectAll().where { (TodoTable.id eq todoId) and (TodoTable.userId eq userId) }.map {
            toTodo(it)
        }.singleOrNull()
    }

    suspend fun addTodo(todo: TodoRequest): Todo = dbQuery {
        val newId = TodoTable.insertAndGetId {
            it[userId] = todo.userId
            it[title] = todo.title
            it[description] = todo.description
            it[completed] = todo.completed
        }.value
        Todo(
            id = newId,
            userId = todo.userId,
            title = todo.title,
            description = todo.description,
            completed = todo.completed
        )
    }

    suspend fun updateTodo(id: Int, todo: TodoRequest, authenticatedUserId: Int): Todo? = dbQuery {
        TodoTable.updateReturning(
            where = { (TodoTable.id eq id) and (TodoTable.userId eq authenticatedUserId) }
        ) {
            it[title] = todo.title
            it[description] = todo.description
            it[completed] = todo.completed
        }.singleOrNull()?.let { toTodo(it) }
    }

    suspend fun deleteTodo(todoId: Int, userId: Int): Boolean = dbQuery {
        TodoTable.deleteWhere {
            (TodoTable.id eq todoId) and (TodoTable.userId eq userId)
        } > 0
    }

    private fun toTodo(row: ResultRow): Todo = Todo(
        id = row[TodoTable.id].value,
        userId = row[TodoTable.userId].value,
        title = row[TodoTable.title],
        description = row[TodoTable.description],
        completed = row[TodoTable.completed]
    )
}