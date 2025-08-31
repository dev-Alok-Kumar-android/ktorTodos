package com.alokkumar.repository

import com.alokkumar.data.dto.User
import com.alokkumar.data.dto.UserRegisterRequest
import com.alokkumar.data.dto.UserResponse
import com.alokkumar.data.tables.UserTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll

class UserRepository {

    suspend fun findUserByEmail(email: String): User? = DatabaseFactory.dbQuery {
        UserTable.selectAll().where { UserTable.email eq email }
            .singleOrNull()
            ?.let { toUser(it) }
    }

    suspend fun findUserById(id: Int): User? = DatabaseFactory.dbQuery {
        UserTable.selectAll().where { UserTable.id eq id }
            .singleOrNull()
            ?.let { toUser(it) }
    }

    suspend fun addUser(user: UserRegisterRequest): UserResponse = DatabaseFactory.dbQuery {
        val newId = UserTable.insertAndGetId {
            it[name] = user.name
            it[email] = user.email
            it[password] = user.password
        }.value
        UserResponse(
            id = newId,
            name = user.name,
            email = user.email
        )
    }

    private fun toUser(row: ResultRow): User = User(
        id = row[UserTable.id].value,
        name = row[UserTable.name],
        email = row[UserTable.email],
        password = row[UserTable.password]
    )
}