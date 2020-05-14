package io.bahlsenwitz.springer.controller.player.update

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.controller.Output
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerProfile(private val repositoryPlayer: RepositoryPlayer) {

    private val dateTime: DateTime = DateTime()
    private val output: Output =
        Output(repositoryPlayer)

    data class UpdateAvatar(
        val id: String,
        val avatar: String
    )

    fun avatar(updateAvatar: UpdateAvatar): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(updateAvatar.id)!!).get()
        player.avatar = updateAvatar.avatar.replace("\n","")
        return output.player(route = "avatar", player = player)
    }

    fun clear(device: String): ResponseEntity<Any> {
        val player: Player? = repositoryPlayer.findByDevice(device)
        if (player != null) {
            player.device = null
            player.updated = dateTime.getDate()
            repositoryPlayer.save(player)
            return output.terminal(result = "success", route = "clear", player = player)
        }
        return output.terminal(result = "fail", route = "clear")
    }

}

