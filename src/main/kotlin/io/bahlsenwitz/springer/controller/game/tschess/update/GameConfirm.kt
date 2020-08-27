package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import org.springframework.http.ResponseEntity
import java.util.*

class GameConfirm(private val repositoryGame: RepositoryGame) {

    private val output: Output = Output(repositoryGame = repositoryGame)

    data class UpdateConfirm(
        val id_game: String,
        val white: Boolean
    )

    fun confirm(updateConfirm: UpdateConfirm): ResponseEntity<Any>? {
        val game: Game = repositoryGame.findById(UUID.fromString(updateConfirm.id_game)!!).get()



        return output.update(route = "confirm", game = game)
    }
}