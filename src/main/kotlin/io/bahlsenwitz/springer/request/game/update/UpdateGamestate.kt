package io.bahlsenwitz.springer.request.game.update

data class UpdateGamestate(
    val id: String,
    val state: List<List<String>>,

    val highlight: String,

    val white_update: String,
    val black_update: String,

    val check_on: String,
    val turn: String,
    val winner: String,

    val catalyst: String,
    val updated: String,

    val notification_id: String,
    val notification_name: String
)