package io.bahlsenwitz.springer.controller.player.path

import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Churn
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerQuick(private val repositoryPlayer: RepositoryPlayer, private val repositoryGame: RepositoryGame? = null) {

    private val output: Output = Output(repositoryPlayer)
    private val churn: Churn = Churn(repositoryPlayer)

    fun quick(id: String): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(id)!!).get()
        val opponent: Player = churn.calculate(player)
        return output.quick(player, opponent)
    }

}