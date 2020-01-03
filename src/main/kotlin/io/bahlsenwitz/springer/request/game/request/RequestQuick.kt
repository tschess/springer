package io.bahlsenwitz.springer.request.game.request

data class RequestQuick(
    val white_uuid: String,
    val black_uuid: String,
    val state: List<List<String>>,
    val skin: String,
    val clock: Int,
    val black_update: String,
    val updated: String,
    val created: String
)