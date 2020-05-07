package io.bahlsenwitz.springer.controller.player.start

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Output
import org.springframework.http.ResponseEntity

class PlayerInit(private val repositoryPlayer: RepositoryPlayer) {

    private val output: Output = Output()
    private val dateTime: DateTime = DateTime()

    fun device(device: String): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findByDevice(device) ?: return output.fail(route = "device")
        player.date = dateTime.getDate()
        repositoryPlayer.save(player)
        return output.player(route = "device", player = player)
    }
}