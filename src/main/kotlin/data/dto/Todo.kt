package com.alokkumar.data.dto

import kotlinx.serialization.Serializable
import nonapi.io.github.classgraph.json.Id

@Serializable
data class Todo(
    @Id val id: Int,
    val userId: Int,
    val title: String,
    val description: String = "",
    val completed: Boolean = false
)
