package com.alokkumar.data

import org.jetbrains.exposed.dao.id.EntityID

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val password: String
)