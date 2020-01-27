package io.bahlsenwitz.springer.controller.player.profile

import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerProfile(private val repositoryPlayer: RepositoryPlayer) {

    fun clear(device: String): ResponseEntity<Any> {
        val player = repositoryPlayer.getByDevice(device)
        if(player != null){
            player.device = "TBD"
            return ResponseEntity.ok(repositoryPlayer.save(player))
        }
        return ResponseEntity.ok("{\"result\": \"ok\"}")
    }

    fun avatar(updateAvatar: UpdateAvatar): ResponseEntity<Any> {
        val player = repositoryPlayer.getById(UUID.fromString(updateAvatar.id))
        player.avatar = updateAvatar.avatar
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    data class UpdateAvatar (
        val id: String,
        val avatar: String
    )
}

