package io.bahlsenwitz.springer.controller.player.start

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid

class PlayerStart(private val repositoryPlayer: RepositoryPlayer) {

    fun login(@Valid @RequestBody requestLogin: RequestLogin): ResponseEntity<Any> {
        val username: String = requestLogin.username
        val password: String = requestLogin.password
        val device: String = requestLogin.device

        val playerD: Player? = repositoryPlayer.findByDevice(device)
        if(playerD != null){
            playerD.device = null
            repositoryPlayer.save(playerD)
        }
        val updated = DateTime().getDate()
        val player: Player = repositoryPlayer.findByUsername(username)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"nonexistent\"}")

        Influx().activity(player_id = player.id.toString(), route = "login")
        if (BCryptPasswordEncoder().matches(password, player.password)) {
            player.device = device
            player.updated = updated
            return ResponseEntity.ok(repositoryPlayer.save(player))
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"password\"}")
    }

    data class RequestLogin (
        val username: String,
        val password: String,
        val device: String
    )
}

