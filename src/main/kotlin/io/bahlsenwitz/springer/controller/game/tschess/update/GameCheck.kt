package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.util.*

class GameCheck(private val repositoryGame: RepositoryGame) {

    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()

    fun check(id_game: String): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(id_game)!!).get()
        if (game.on_check) {
            return ResponseEntity.ok(ResponseEntity.accepted())
        }
        game.on_check = true
        game.updated = dateTime.getDate()
        influx.game(game, "check")
        repositoryGame.save(game)
        return ResponseEntity.ok(ResponseEntity.accepted())
    }

}