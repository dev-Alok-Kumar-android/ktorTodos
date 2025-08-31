package com.alokkumar.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserCredential(
    val email: String,
    val password: String
)