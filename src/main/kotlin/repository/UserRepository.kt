package com.alokkumar.repository

import com.alokkumar.data.User
import com.alokkumar.data.tables.UserTable
import com.alokkumar.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll

class UserRepository {

    suspend fun findUserByEmail(email: String): User? = dbQuery {
        UserTable.selectAll().where { UserTable.email eq email }
            .singleOrNull()
            ?.let { toUser(it) }
    }

    suspend fun findUserById(id: Int): User? = dbQuery {
        UserTable.selectAll().where { UserTable.id eq id }
            .singleOrNull()
            ?.let { toUser(it) }
    }

    suspend fun addUser(user: User): User = dbQuery {
        val newId = UserTable.insertAndGetId {
            it[name] = user.name
            it[email] = user.email
            it[password] = user.password
        }.value
        user.copy(id = newId)
    }

    private fun toUser(row: ResultRow): User = User(
        id = row[UserTable.id].value,
        name = row[UserTable.name],
        email = row[UserTable.email],
        password = row[UserTable.password]
    )
}