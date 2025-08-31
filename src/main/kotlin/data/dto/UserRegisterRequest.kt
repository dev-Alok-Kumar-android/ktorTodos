package com.alokkumar.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterRequest(
    val name: String,
    val email: String,
    val password: String
)