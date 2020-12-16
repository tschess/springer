package io.bahlsenwitz.springer.controller.player.request

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerPush(private val repositoryPlayer: RepositoryPlayer) {

    data class UpdatePush(
        val id: String,
        val note_key: String
    )

    fun push(updatePush: UpdatePush): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(updatePush.id)!!).get()
        player.note_key = updatePush.note_key
        //return output.player(player = player, route = "push")
        player.updated = DateTime().getDate()
        return ResponseEntity.ok().body(repositoryPlayer.save(player))
    }

}