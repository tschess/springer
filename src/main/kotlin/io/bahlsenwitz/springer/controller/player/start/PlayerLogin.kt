package io.bahlsenwitz.springer.controller.player.start

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class PlayerLogin(private val repositoryPlayer: RepositoryPlayer) {

    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()

    fun login(requestLogin: RequestStart): ResponseEntity<Any> {
        val player00: Player? = repositoryPlayer.findByDevice(requestLogin.device)
        if (player00 != null) {
            player00.device = null
            repositoryPlayer.save(player00)
        }
        val player: Player = repositoryPlayer.findByUsername(requestLogin.username)
            ?: return ResponseEntity.ok(ResponseEntity.notFound())
        influx.activity(player, "login")
        if (BCryptPasswordEncoder().matches(requestLogin.password, player.password)) {
            player.device = requestLogin.device
            player.updated = dateTime.getDate()
            return ResponseEntity.ok(repositoryPlayer.save(player))
        }
        return ResponseEntity.ok(ResponseEntity.badRequest())
    }
}

