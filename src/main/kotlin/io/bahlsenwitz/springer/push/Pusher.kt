package io.bahlsenwitz.springer.push

import io.bahlsenwitz.springer.model.player.Player

class Pusher() {

    fun notify(player: Player) {
        val pushRunnable: PushRunnable = PushRunnable(player)
        pushRunnable.run()
    }

}

class PushRunnable(val player: Player) : Runnable {
    override fun run() {
        try {
            val key: String = player.note_key ?: return
            val command: List<String?> = listOf(
                "/home/ubuntu/springer/src/main/kotlin/io/bahlsenwitz/springer/push/ios.sh",
                key,
                "{\"aps\":{" +
                        "\"alert\":{\"body\":\"Your move.\"}," +
                        "\"badge\":\"1\"," +
                        "\"content-available\":1" +
                        "}}"
            )
            ProcessBuilder(command).start()
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }
}