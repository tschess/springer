package io.bahlsenwitz.springer.controller.player.start

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.validation.Valid

class PlayerStart(private val repositoryPlayer: RepositoryPlayer) {

    fun login(@Valid @RequestBody requestLogin: RequestLogin): ResponseEntity<Any> {
        val username: String = requestLogin.username
        val password: String = requestLogin.password

        val device: String = requestLogin.device
        val playerD: Player? = repositoryPlayer.findByDevice(device)
        if(playerD != null){
            playerD.device = "TBD"
            repositoryPlayer.save(playerD)
        }

        val updated: Date = Date.from(ZonedDateTime.now(ZoneId.of("America/New_York")).toInstant())

        val player: Player = repositoryPlayer.findByUsername(username)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"nonexistent\"}")

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

