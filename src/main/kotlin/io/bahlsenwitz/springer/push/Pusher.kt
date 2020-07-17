package io.bahlsenwitz.springer.push

import io.bahlsenwitz.springer.model.player.Player

class Pusher() {
    companion object {
        const val path: String = "/home/ubuntu/springer/src/main/kotlin/io/bahlsenwitz/springer/push/"
    }

    fun notify(player: Player) {
        val key: String = player.note_key ?: return
        val prefix: String = key.substring(0, 8)
        val runnable: Runnable = if (prefix == "ANDROID_") {
            val test: String = key.removePrefix(prefix)
            RunnableAndroid(test)
        } else {
            RunnableIos(key)
        }
        runnable.run()
    }
}

class RunnableIos(val key: String) : Runnable {
    override fun run() {
        try {
            val command: List<String?> = listOf(
                "${Pusher.path}ios.sh",
                key,
                "{\"aps\":{\"alert\":{\"body\":\"Your move.\"},\"badge\":\"1\",\"content-available\":1}}"
            )
            ProcessBuilder(command).start()
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }
}

class RunnableAndroid(val key: String) : Runnable {
    override fun run() {
        try {
            val command: List<String?> = listOf(
                "${Pusher.path}android.sh",
                key
            )
            ProcessBuilder(command).start()
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }
}


