package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Tschess
import org.springframework.http.ResponseEntity
import java.util.*

class GameProp(private val repositoryGame: RepositoryGame) {

    private val influx: Influx = Influx()
    private val tschess: Tschess = Tschess()
    private val dateTime: DateTime = DateTime()

    fun prop(id_game: String): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(id_game)!!).get()
        game.highlight = "TBD"
        game.updated = dateTime.getDate()
        game.condition = CONDITION.PENDING
        game.turn = tschess.setTurn(game.turn)

        repositoryGame.save(game)
        influx.game(game, "prop")
        return ResponseEntity.ok(ResponseEntity.accepted())
    }

}