package io.bahlsenwitz.springer.controller.player.update

import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerPush(private val repositoryPlayer: RepositoryPlayer) {

    private val output: Output = Output(repositoryPlayer)

    data class UpdatePush(
        val id: String,
        val note_key: String
    )

    fun push(updatePush: UpdatePush): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(updatePush.id)!!).get()
        player.note_key = updatePush.note_key
        return output.player(route = "push", player = player)
    }

}