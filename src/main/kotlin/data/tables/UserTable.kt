package com.alokkumar.data.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object UserTable : IntIdTable("user") {
    val name: Column<String> = varchar("name", 255)
    val email: Column<String> = varchar("email", 255)
    val password: Column<String> = varchar("password", 255)
}