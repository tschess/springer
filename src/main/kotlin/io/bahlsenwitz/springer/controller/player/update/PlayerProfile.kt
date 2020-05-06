package io.bahlsenwitz.springer.controller.player.update

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerProfile(private val repositoryPlayer: RepositoryPlayer) {

    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()

    data class UpdateAvatar(
        val id: String,
        val avatar: String
    )

    fun avatar(updateAvatar: UpdateAvatar): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(updateAvatar.id)!!).get()
        player.avatar = updateAvatar.avatar
        player.updated = dateTime.getDate()
        influx.activity(player, "avatar")
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    fun clear(device: String): ResponseEntity<Any> {
        val player: Player? = repositoryPlayer.findByDevice(device)
        if (player != null) {
            player.device = null
            player.updated = dateTime.getDate()
            influx.activity(player, "clear")
            repositoryPlayer.save(player)
            return ResponseEntity.ok(ResponseEntity.accepted())
        }
        return ResponseEntity.ok(ResponseEntity.badRequest())
    }

}

