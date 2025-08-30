package com.alokkumar.data.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TodoTable : IntIdTable("TodoTable") {
    val userId: Column<Int> = integer("userId").references(UserTable.id)
    val title: Column<String> = varchar("title", 255)
    val description: Column<String> = varchar("description", 512).default("")
    val completed: Column<Boolean> = bool("completed")
}