package com.alokkumar.repository

import com.alokkumar.data.Todo
import com.alokkumar.data.tables.TodoTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TodoRepository {

    fun getAllTodos(): List<Todo> = transaction {
        TodoTable.selectAll().map { toTodo(it) }
    }

    fun getTodo(id: Int): Todo? = transaction {
        TodoTable.selectAll().where { TodoTable.id eq id }.singleOrNull()?.let { toTodo(it) }
    }

    fun addTodo(todo: Todo): Todo = transaction {
        val newId = TodoTable.insertAndGetId {
            it[userId] = todo.userId
            it[title] = todo.title
            it[description] = todo.description
            it[completed] = todo.completed
        }.value
        todo.copy(id = newId)
    }

    fun updateTodo(id: Int, todo: Todo): Todo? = transaction {
        val rowsAffected = TodoTable.update({ TodoTable.id eq id }) {
            it[title] = todo.title
            it[description] = todo.description
            it[completed] = todo.completed
        }
        if (rowsAffected > 0) todo.copy(id = id) else null
    }

    fun deleteTodo(id: Int): Boolean = transaction {
        TodoTable.deleteWhere { TodoTable.id eq id } > 0
    }

    private fun toTodo(row: ResultRow): Todo = Todo(
        id = row[TodoTable.id].value,
        userId = row[TodoTable.userId],
        title = row[TodoTable.title],
        description = row[TodoTable.description],
        completed = row[TodoTable.completed]
    )
}