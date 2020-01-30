package io.bahlsenwitz.springer.controller.player.profile

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerProfile(private val repositoryPlayer: RepositoryPlayer) {

    fun clear(device: String): ResponseEntity<Any> {
        val player: Player? = repositoryPlayer.findByDevice(device)
        if(player != null){
            player.device = "TBD"
            return ResponseEntity.ok(repositoryPlayer.save(player))
        }
        return ResponseEntity.ok("{\"result\": \"ok\"}")
    }

    fun avatar(updateAvatar: UpdateAvatar): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(updateAvatar.id)!!
        val player: Player = repositoryPlayer.findById(uuid).get()
        player.avatar = updateAvatar.avatar
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    data class UpdateAvatar (
        val id: String,
        val avatar: String
    )
}

