package io.bahlsenwitz.springer.request.game.update

data class UpdateUnilateral(
    val uuid_game: String,
    val uuid_player: String,
    val updated: String
)