package io.bahlsenwitz.springer.request.game.request

data class RequestTest(
    val state: List<List<String>>?,
    val catalyst: String,
    val check_on: String,
    val winner: String,
    val updated: String,
    val created: String
)