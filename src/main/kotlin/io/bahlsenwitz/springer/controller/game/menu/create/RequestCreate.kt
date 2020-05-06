package io.bahlsenwitz.springer.controller.game.menu.create

data class RequestCreate(
    val id_self: String,
    val id_other: String,
    val config: Int
)