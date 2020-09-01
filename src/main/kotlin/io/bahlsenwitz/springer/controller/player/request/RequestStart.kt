package io.bahlsenwitz.springer.controller.player.request

data class RequestStart(
    val username: String,
    val password: String,
    val device: String
)