package io.bahlsenwitz.springer.controller.game.request

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.controller.game.util.GameTurn
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameEval(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val dateTime: DateTime = DateTime()
    private val gameTurn: GameTurn = GameTurn(repositoryPlayer)
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    data class EvalUpdate(
        val id_game: String,
        val id_self: String,
        val accept: Boolean
    )

    fun eval(evalUpdate: EvalUpdate): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(evalUpdate.id_game)!!).get()
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(evalUpdate.id_self)!!).get()
        val date: String = dateTime.getDate()
        val accept: Boolean = evalUpdate.accept
        if (!accept) {
            game.condition = CONDITION.TBD
            game.turn = gameTurn.setTurn(game = game)
            //return output.update(route = "eval", game = game)
            repositoryGame.save(game)
            val body: MutableMap<String, String> = HashMap()
            body["success"] = "eval"
            return ResponseEntity.ok().body(body)
        }
        playerSelf.updated = date
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.DRAW
        rating.draw(game) //also persists game/players
        //return output.update(route = "eval", game = game)
        val body: MutableMap<String, String> = HashMap()
        body["success"] = "eval"
        return ResponseEntity.ok().body(body)
    }


}