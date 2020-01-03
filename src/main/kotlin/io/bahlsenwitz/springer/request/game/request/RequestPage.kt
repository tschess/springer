package io.bahlsenwitz.springer.request.game.request

data class RequestPage(
    val id: String,
    val page: Int,
    val size: Int
)