package io.bahlsenwitz.springer.request.game.update

data class UpdateMessage(
    val id_game: String,
    val id_player: String,
    val message: String,
    val posted: String)