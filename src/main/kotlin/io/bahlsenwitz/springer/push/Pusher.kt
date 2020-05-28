package io.bahlsenwitz.springer.push

import io.bahlsenwitz.springer.model.player.Player


class Pusher() {

    fun notify(player: Player) {
        val key: String = player.note_key ?: return
        val command: List<String?> = listOf(
            "/home/ubuntu/springer/src/main/kotlin/io/bahlsenwitz/springer/push/pu.sh",
            key,
            "{\"aps\":{\"alert\":\"your move...\",\"badge\":\"1\",\"content-available\":1}}"
        )
        ProcessBuilder(command).start()
    }

}