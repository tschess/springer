package io.bahlsenwitz.springer.controller.game.response

import java.util.*

class GameHistoric(
    val id: UUID,
    val state: List<List<String>>,
    val status: String,
    val white_name: String,
    val black_name: String,
    var winner: String,
    var catalyst: String,
    val skin: String,
    var created: String,
    val opponent_id: UUID,
    val opponent_name: String,
    val opponent_avatar: String,
    val opponent_rank: Int,
    val opponent_elo: Int)