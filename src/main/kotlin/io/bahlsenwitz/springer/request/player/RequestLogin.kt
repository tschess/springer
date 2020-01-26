package io.bahlsenwitz.springer.request.player

data class RequestLogin (
    val username: String,
    val password: String,
    val device: String,
    val updated: String
)