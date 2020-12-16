package io.bahlsenwitz.springer.controller.game.request

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Config
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameMine(
    private val repositoryGame: RepositoryGame,
    repositoryPlayer: RepositoryPlayer
) {

    private val config: Config = Config()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    data class UpdateMine(val id_game: String, val state: List<List<String>>)

    fun mine(updateMine: UpdateMine): ResponseEntity<Any>? {
        val game: Game = repositoryGame.findById(UUID.fromString(updateMine.id_game)!!).get()
        game.highlight = "TBD"
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.LANDMINE
        game.state = config.poisonReveal(updateMine.state)
        game.updated = DateTime().getDate()
        rating.resolve(game)
        //return output.update(route = "mine", game = game)
        val body: MutableMap<String, String> = HashMap()
        body["success"] = "mine"
        return ResponseEntity.ok().body(body)
    }

}