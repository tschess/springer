package io.bahlsenwitz.springer.controller.player.start

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.controller.Output
import org.springframework.http.ResponseEntity

class PlayerInit(private val repositoryPlayer: RepositoryPlayer) {

    private val dateTime: DateTime = DateTime()
    private val output: Output =
        Output(repositoryPlayer)

    fun device(device: String): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findByDevice(device) ?: return output.terminal(result = "fail", route = "device")
        player.date = dateTime.getDate()
        return output.player(player)
    }
}