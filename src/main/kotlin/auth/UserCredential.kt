package com.alokkumar.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserCredential(
    val email: String,
    val password: String
)