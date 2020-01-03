package io.bahlsenwitz.springer.request.game.update

data class UpdateResolve(
    val id: String,
    val winner: String,
    val notification_id: String,
    val notification_name: String,
    val catalyst: String,
    val updated: String
)