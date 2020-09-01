package io.bahlsenwitz.springer.controller.game.request

data class RequestCreate(
    val id_self: String,
    val id_other: String,
    val config: Int
)