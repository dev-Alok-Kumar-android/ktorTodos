package com.alokkumar.data

data class Todo(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val completed: Boolean
)
