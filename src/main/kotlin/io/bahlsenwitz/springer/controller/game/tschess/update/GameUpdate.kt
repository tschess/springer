package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.Output
import io.bahlsenwitz.springer.util.Tschess
import org.springframework.http.ResponseEntity
import java.util.*

class GameUpdate(private val repositoryGame: RepositoryGame) {

    private val tschess: Tschess = Tschess()
    private val output: Output = Output(repositoryGame = repositoryGame)

    data class UpdateGame(
        val id_game: String,
        val state: List<List<String>>,
        val highlight: String,
        val condition: String
    )

    fun update(updateGame: UpdateGame): ResponseEntity<Any>? {
        val game: Game = repositoryGame.findById(UUID.fromString(updateGame.id_game)).get()
        if (game.status == STATUS.RESOLVED) {
            return null
        }
        game.moves += 1
        game.on_check = false
        game.state = updateGame.state
        game.highlight = updateGame.highlight
        game.turn = tschess.setTurn(turn = game.turn)
        game.condition = CONDITION.valueOf(updateGame.condition)
        return output.update(route = "update", game = game)
    }

}