package io.bahlsenwitz.springer.controller.player.start

import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity

class PlayerKey(private val repositoryPlayer: RepositoryPlayer) {

    private val output: Output = Output(repositoryPlayer)

    data class UpdateKey(
        val device: String,
        val note_key: String
    )

    fun note(updateKey: UpdateKey): ResponseEntity<Any> {
        val player: Player =
            repositoryPlayer.findByDevice(updateKey.device) ?: return output.terminal(result = "fail", route = "key")
        player.note_key = updateKey.note_key
        return output.player(route = "key", player = player)
    }
}