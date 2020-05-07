package io.bahlsenwitz.springer.controller.player.start

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity

class PlayerInit(private val repositoryPlayer: RepositoryPlayer) {

    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()

    fun device(device: String): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findByDevice(device) ?: return ResponseEntity.ok(ResponseEntity.notFound())
        player.date = dateTime.getDate()
        repositoryPlayer.save(player)
        influx.activity(player, "device")
        return ResponseEntity.ok(player)
    }
}