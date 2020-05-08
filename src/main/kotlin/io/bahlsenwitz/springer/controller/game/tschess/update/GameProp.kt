package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.Output
import io.bahlsenwitz.springer.util.Tschess
import org.springframework.http.ResponseEntity
import java.util.*

class GameProp(private val repositoryGame: RepositoryGame) {

    private val tschess: Tschess = Tschess()
    private val output: Output = Output(repositoryGame = repositoryGame)

    fun prop(id_game: String): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(id_game)!!).get()
        game.highlight = "TBD"
        game.condition = CONDITION.PENDING
        game.turn = tschess.setTurn(game.turn)
        return output.update(route = "prop", game = game)
    }

}