package io.bahlsenwitz.springer.controller.game.response

import java.util.*

class GameActual(
    val id: UUID,
    val config: List<List<String>>,
    val status: String,
    val turn: String,
    val catalyst: String,
    val skin: String,
    val created: String,

    val white_name: String,
    val black_name: String,

    val white_message: String,
    val white_seen: Boolean,
    val white_posted: String,
    val black_message: String,
    val black_seen: Boolean,
    val black_posted: String,

    val opponent_id: UUID,
    val opponent_name: String,
    val opponent_avatar: String,
    val opponent_rank: Int,
    val opponent_elo: Int,
    val inbound: Boolean)