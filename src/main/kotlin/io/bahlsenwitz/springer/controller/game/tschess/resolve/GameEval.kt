package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Output
import io.bahlsenwitz.springer.util.Rating
import io.bahlsenwitz.springer.util.Tschess
import org.springframework.http.ResponseEntity
import java.util.*

class GameEval(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val tschess: Tschess = Tschess()
    private val dateTime: DateTime = DateTime()
    private val output: Output = Output(repositoryGame = repositoryGame)
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    data class EvalUpdate(
        val id_game: String,
        val id_self: String,
        val id_other: String,
        val accept: Boolean,
        val white: Boolean
    )

    fun eval(evalUpdate: EvalUpdate): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(evalUpdate.id_game)!!).get()
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(evalUpdate.id_self)!!).get()
        val playerOther: Player = repositoryPlayer.findById(UUID.fromString(evalUpdate.id_other)!!).get()
        playerOther.note = true
        val date: String = dateTime.getDate()
        playerSelf.updated = date
        val accept: Boolean = evalUpdate.accept
        if (!accept) {
            game.condition = CONDITION.TBD
            game.turn = tschess.setTurn(game.turn)
            repositoryPlayer.saveAll(listOf(playerSelf, playerOther))
            return output.update(route = "eval", game = game)
        }
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.DRAW
        rating.draw(game) //also persists game/players
        return output.terminal(result = "success", route = "eval")
    }


}