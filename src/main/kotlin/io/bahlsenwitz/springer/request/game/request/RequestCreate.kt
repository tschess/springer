package io.bahlsenwitz.springer.request.game.request

data class RequestCreate(
    val white_uuid: String,
    val black_uuid: String,
    val clock: Int,
    val config: String,
    val updated: String,
    val created: String
)
