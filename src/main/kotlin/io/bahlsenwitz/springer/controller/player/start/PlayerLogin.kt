package io.bahlsenwitz.springer.controller.player.start

import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class PlayerLogin(private val repositoryPlayer: RepositoryPlayer) {

    private val output: Output = Output(repositoryPlayer)

    fun login(requestLogin: RequestStart): ResponseEntity<Any> {
        val player00: Player? = repositoryPlayer.findByDevice(requestLogin.device)
        if (player00 != null) {
            player00.device = null
            repositoryPlayer.save(player00)
        }
        val player: Player =
            repositoryPlayer.findByUsername(requestLogin.username) ?: return output.terminal(
                result = "fail",
                route = "login"
            )
        if (BCryptPasswordEncoder().matches(requestLogin.password, player.password)) {
            player.device = requestLogin.device

            if(requestLogin.note_key != "NULL"){
                player.note_key = requestLogin.note_key
            }

            return output.player(route = "login", player = player)
        }
        return output.terminal(result = "fail", route = "login")
    }
}

