package io.bahlsenwitz.springer.controller.game.path

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
    repositoryPlayer: RepositoryPlayer) {

    private val config: Config = Config()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun timeout(id_game: String): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(id_game)!!).get()
        game.highlight = "TBD"
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.TIMEOUT
        game.state = config.poisonReveal(game.state!!)
        rating.resolve(game) //TODO: Contains "repositoryGame!!.save(game)"
        //return output.update(route = "timeout", game = game)
        val body: MutableMap<String, String> = HashMap()
        body["success"] = "timeout"
        return ResponseEntity.ok().body(body)
    }
}