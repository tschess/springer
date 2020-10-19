package io.bahlsenwitz.springer.controller.game.path

import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Config
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameTimeout(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer) {

    private val config: Config = Config()
    private val output: Output = Output(repositoryGame = repositoryGame)
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun timeout(id_game: String): ResponseEntity<Any>? {
        val game: Game = repositoryGame.findById(UUID.fromString(id_game)!!).get()
        game.highlight = "TBD"
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.TIMEOUT
        game.state = config.poisonReveal(game.state!!)
        rating.resolve(game)
        return output.update(route = "timeout", game = game)
    }
}