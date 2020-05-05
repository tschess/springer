package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameMate(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun mate(id_game: String): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(id_game)!!).get()
        influx.game(game, "mate")

        if (game.status == STATUS.RESOLVED) {
            return ResponseEntity.ok(ResponseEntity.accepted())
        }
        val date: String = dateTime.getDate()
        game.updated = date
        game.highlight = "TBD"
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.CHECKMATE

        rating.mate(game)
        return ResponseEntity.ok(ResponseEntity.accepted())
    }
}