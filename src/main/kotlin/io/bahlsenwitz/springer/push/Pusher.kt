package io.bahlsenwitz.springer.push

import io.bahlsenwitz.springer.model.player.Player
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class Pusher {

    private val development: String = "api.sandbox.push.apple.com:443"
    private val production: String = "api.push.apple.com:443"

    private val key: Key = Key()

    fun ios(player: Player) {
//soLaLa
        //headers: Map<String, String> = mapOf(), params: Map<String, String> = mapOf(),

        val path: String = "/3/device/"

        try {
            khttp.post(
                headers = "",
                url = "$development$path${player.note_key}",
                data =
            )
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

    fun getSha256Hash(): String? {
        return try {
            var digest: MessageDigest? = null
            try {
                digest = MessageDigest.getInstance("SHA-256")
            } catch (e1: NoSuchAlgorithmException) {
                e1.printStackTrace()
            }
            digest!!.reset()
            bin2hex(digest.digest(key.value.toByteArray()))
        } catch (ignored: java.lang.Exception) {
            null
        }
    }

    private fun bin2hex(data: ByteArray): String? {
        val hex = StringBuilder(data.size * 2)
        for (b: Byte in data) hex.append(String.format("%02x", (b.toInt() and 0x0f).toByte()))
        return hex.toString()
    }
}