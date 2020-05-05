package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameEval(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()
    private val rating: Rating = Rating(repositoryPlayer)

    data class EvalUpdate(
        val id_game: String,
        val id_self: String,
        val id_other: String,
        val accept: Boolean,
        val white: Boolean
    )

    fun eval(evalUpdate: EvalUpdate): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(evalUpdate.id_game)!!).get()
        influx.game(game, "eval")
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(evalUpdate.id_self)!!).get()
        val playerOther: Player = repositoryPlayer.findById(UUID.fromString(evalUpdate.id_other)!!).get()
        playerOther.note = true

        val date: String = dateTime.getDate()
        game.updated = date
        playerSelf.updated = date

        val accept: Boolean = evalUpdate.accept
        if (!accept) {
            game.condition = CONDITION.TBD
            game.turn = setTurn(game.turn)
            repositoryGame.save(game)
            repositoryPlayer.saveAll(listOf(playerSelf, playerOther))
            return ResponseEntity.ok(ResponseEntity.accepted())
        }
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.DRAW
        rating.draw(playerSelf, playerOther, game, evalUpdate.white)

        influx.game(game, "eval")
        return ResponseEntity.ok(ResponseEntity.accepted())
    }

    private fun setTurn(turn: CONTESTANT): CONTESTANT {
        if (turn == CONTESTANT.WHITE) {
            return CONTESTANT.BLACK
        }
        return CONTESTANT.WHITE
    }
}