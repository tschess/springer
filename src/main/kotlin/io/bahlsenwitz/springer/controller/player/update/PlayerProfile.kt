package io.bahlsenwitz.springer.controller.player.update

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Output
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerProfile(private val repositoryPlayer: RepositoryPlayer) {

    private val output: Output = Output(repositoryPlayer)
    private val dateTime: DateTime = DateTime()

    data class UpdateAvatar(
        val id: String,
        val avatar: String
    )

    fun avatar(updateAvatar: UpdateAvatar): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(updateAvatar.id)!!).get()
        player.avatar = updateAvatar.avatar
        player.updated = dateTime.getDate()
        return output.player(route = "avatar", player = player)
    }

    fun clear(device: String): ResponseEntity<Any> {
        val player: Player? = repositoryPlayer.findByDevice(device)
        if (player != null) {
            player.device = null
            player.updated = dateTime.getDate()
            return output.success(route = "clear", player = player)
        }
        return output.fail(route = "clear")
    }

}

