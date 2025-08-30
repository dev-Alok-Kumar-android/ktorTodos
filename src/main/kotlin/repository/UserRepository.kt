package com.alokkumar.repository

import com.alokkumar.data.User
import com.alokkumar.data.tables.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {

    fun findUserByEmail(email: String): User? = transaction {
        UserTable.select { UserTable.email eq email }
            .singleOrNull()
            ?.let { toUser(it) }
    }

    fun findUserById(id: Int): User? = transaction {
        UserTable.select { UserTable.id eq id }
            .singleOrNull()
            ?.let { toUser(it) }
    }

    fun addUser(user: User): User = transaction {
        val newId = UserTable.insertAndGetId {
            it[name] = user.name
            it[email] = user.email
            it[password] = user.password // Note: You'll need to add a password column to your UserTable
        }.value
        user.copy(id = newId)
    }

    private fun toUser(row: ResultRow): User = User(
        id = row[UserTable.id].value,
        name = row[UserTable.name],
        email = row[UserTable.email],
        password = row[UserTable.password] // Add this to your User data class
    )
}