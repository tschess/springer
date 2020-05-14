package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameMate(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val output: Output =
        Output(repositoryGame = repositoryGame)
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun mate(id_game: String): ResponseEntity<Any>? {
        val game: Game = repositoryGame.findById(UUID.fromString(id_game)!!).get()
        if (game.status == STATUS.RESOLVED) {
            return output.terminal(result = "fail", route = "mate")
        }
        game.highlight = "TBD"
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.CHECKMATE
        rating.resolve(game)
        return output.update(route = "mate", game = game)
    }
}