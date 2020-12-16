package io.bahlsenwitz.springer.controller.game.path

import io.bahlsenwitz.springer.controller.game.util.GameTurn
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class GameProp(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val gameTurn: GameTurn = GameTurn(repositoryPlayer)
    private val output: Output =
        Output(repositoryGame = repositoryGame)

    fun prop(id_game: String): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(id_game)!!).get()
        game.highlight = "TBD"
        game.condition = CONDITION.PENDING
        game.turn = gameTurn.setTurn(game = game)
        return output.update(route = "prop", game = game)
    }

}