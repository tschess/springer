package io.bahlsenwitz.springer.controller.player.util

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerProfile(private val repositoryPlayer: RepositoryPlayer) {

    private val dateTime: DateTime = DateTime()

    data class UpdateAvatar(
        val id: String,
        val avatar: String
    )

    fun avatar(updateAvatar: UpdateAvatar): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(updateAvatar.id)!!).get()
        player.avatar = updateAvatar.avatar.replace("\n", "")
        //return output.player(player = player, route = "avatar")
        player.updated = dateTime.getDate()
        return ResponseEntity.ok().body(repositoryPlayer.save(player))
    }

    fun clear(device: String): ResponseEntity<Any> {
        val player: Player? = repositoryPlayer.findByDevice(device)
        if (player != null) {
            player.device = null
            player.updated = dateTime.getDate()
            repositoryPlayer.save(player)
            //return output.terminal(result = "success", route = "clear")
            val body: MutableMap<String, String> = HashMap()
            body["success"] = "clear"
            return ResponseEntity.ok().body(body)
        }
        //return output.terminal(result = "fail", route = "clear")
        val body: MutableMap<String, String> = HashMap()
        body["fail"] = "clear"
        return ResponseEntity.ok().body(body)
    }

}

