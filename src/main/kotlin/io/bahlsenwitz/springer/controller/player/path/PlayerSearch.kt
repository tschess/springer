package io.bahlsenwitz.springer.controller.player.path

import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity

class PlayerSearch(private val repositoryPlayer: RepositoryPlayer) {

    private val output: Output = Output(repositoryPlayer)

    fun search(username: String): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findByUsername(username)
            ?: return output.terminal(result = "fail", route = "search")
        return output.player(player = player, route = "search")
    }

}