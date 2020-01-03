package io.bahlsenwitz.springer.request.player

data class UpdateConfig (
    val config: List<List<String>>,
    val name: String,
    val id: String,
    val updated: String
)

