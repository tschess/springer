package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.controller.game.tschess.Tschess
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.push.Pusher
import org.springframework.http.ResponseEntity
import java.util.*

class GameUpdate(
    private val repositoryGame: RepositoryGame,
    repositoryPlayer: RepositoryPlayer
) {

    private val tschess: Tschess = Tschess(repositoryPlayer)
    private val output: Output = Output(repositoryGame = repositoryGame)

    data class UpdateGame(
        val id_game: String,
        val state: List<List<String>>,
        val highlight: String,
        val condition: String
    )

    fun update(updateGame: UpdateGame): ResponseEntity<Any>? {
        val game: Game = repositoryGame.findById(UUID.fromString(updateGame.id_game)).get()
        val resolved: Boolean = game.status == STATUS.RESOLVED
        val difference: Boolean = game.state != updateGame.state
        if(resolved || !difference) { // prevent turn from advance on no change...
            return output.terminal(result = "fail", route = "update")
        }
        game.moves += 1
        game.on_check = false
        game.state = updateGame.state
        game.highlight = updateGame.highlight
        game.turn = tschess.setTurn(game = game)
        game.condition = CONDITION.valueOf(updateGame.condition)
        return output.update(route = "update", game = game)
    }

}