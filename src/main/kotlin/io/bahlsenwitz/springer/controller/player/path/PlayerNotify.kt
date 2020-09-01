package io.bahlsenwitz.springer.controller.player.path

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.controller.Output
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerNotify(private val repositoryPlayer: RepositoryPlayer) {

    private val output: Output = Output()

    fun notify(id: String): ResponseEntity<Any>? {
        val player: Player = repositoryPlayer.findById(UUID.fromString(id)!!).get()
        if (!player.note_value) {
            return output.terminal(result = "fail", route = "notify")
        }
        return output.terminal(result = "success", route = "notify")
    }
}