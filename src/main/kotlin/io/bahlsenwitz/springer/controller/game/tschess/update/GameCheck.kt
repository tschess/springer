package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.Output
import org.springframework.http.ResponseEntity
import java.util.*

class GameCheck(private val repositoryGame: RepositoryGame) {

    private val output: Output = Output(repositoryGame = repositoryGame)

    fun check(id_game: String): ResponseEntity<Any>? {
        val game: Game = repositoryGame.findById(UUID.fromString(id_game)!!).get()
        if (game.on_check) {
            return null
        }
        game.on_check = true
        return output.update(route = "check", game = game)
    }

}