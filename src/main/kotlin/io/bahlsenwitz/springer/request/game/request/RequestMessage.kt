package io.bahlsenwitz.springer.request.game.request

data class RequestMessage(
    val id_game: String,
    val white_message: String,
    val black_message: String)