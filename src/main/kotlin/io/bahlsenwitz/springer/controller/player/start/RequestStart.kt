package io.bahlsenwitz.springer.controller.player.start

data class RequestStart(
    val username: String,
    val password: String,
    val device: String
)