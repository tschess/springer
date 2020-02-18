package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.OUTCOME
import io.bahlsenwitz.springer.repository.RepositoryGame
import org.springframework.http.ResponseEntity
import java.util.*

//private val repositoryPlayer: RepositoryPlayer <-- gotta notifyu these guys...
class GameProp(private val repositoryGame: RepositoryGame) {

    val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun prop(id_game: String): ResponseEntity<Any> {
        val uuid0: UUID = UUID.fromString(id_game)!!
        val game: Game = repositoryGame.findById(uuid0).get()

        game.outcome = OUTCOME.PENDING
        game.highlight = "TBD"
        game.turn = setTurn(turn = game.turn)
        game.updated = DATE_TIME_GENERATOR.rightNowString()
        repositoryGame.save(game)
        return ResponseEntity.ok("{\"success\": \"ok\"}") //what does this need to return? the game I guess...
    }

    private fun setTurn(turn: CONTESTANT): CONTESTANT {
        if (turn == CONTESTANT.WHITE) {
            return CONTESTANT.BLACK
        }
        return CONTESTANT.WHITE
    }
}