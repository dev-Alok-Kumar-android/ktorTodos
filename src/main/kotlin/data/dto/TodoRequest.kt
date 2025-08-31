package com.alokkumar.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoRequest(
    val userId: Int,
    val title: String,
    val description: String,
    val completed: Boolean
)