package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Tschess
import org.springframework.http.ResponseEntity
import java.util.*

class GameUpdate(private val repositoryGame: RepositoryGame) {

    private val influx: Influx = Influx()
    private val tschess: Tschess = Tschess()
    private val dateTime: DateTime = DateTime()

    data class UpdateGame(
        val id_game: String,
        val state: List<List<String>>,
        val highlight: String,
        val condition: String
    )

    fun update(updateGame: UpdateGame): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(updateGame.id_game)).get()
        if (game.status == STATUS.RESOLVED) {
            return ResponseEntity.ok(ResponseEntity.accepted())
        }
        game.state = updateGame.state
        game.moves += 1
        game.updated = dateTime.getDate()
        game.turn = tschess.setTurn(turn = game.turn)
        game.highlight = updateGame.highlight
        game.condition = CONDITION.valueOf(updateGame.condition)
        game.on_check = false

        repositoryGame.save(game)
        influx.game(game, "update")
        return ResponseEntity.ok(ResponseEntity.accepted())
    }

}