package io.bahlsenwitz.springer.push


class Pusher() {

    fun test() {

        val command = listOf(
            "/home/ubuntu/springer/src/main/kotlin/io/bahlsenwitz/springer/push/pu.sh",
            "9c06a3a757faf8ee173fb8f6b631160db1a2dd8c43f3f4a083d3dbb88034a76a",
            "{\"aps\":{\"alert\":\"your move...42\",\"badge\":\"1\",\"content-available\":1}}"
        )
        ProcessBuilder(command).start()

    }


}