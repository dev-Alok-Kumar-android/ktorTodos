package com.alokkumar.auth

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

val hashKey = hex(System.getenv("HASH_KEY"))
val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

fun hashPassword(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}