package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.ConfigState
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameMine(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()
    private val configState: ConfigState = ConfigState()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    data class UpdateMine(val id_game: String, val state: List<List<String>>)

    fun mine(updateMine: UpdateMine): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(updateMine.id_game)!!).get()

        game.state = configState.poisonReveal(updateMine.state)
        game.updated = dateTime.getDate()
        game.highlight = "TBD"
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.LANDMINE

        rating.resolve(game)
        repositoryGame.save(game)
        influx.game(game, "mine")
        return ResponseEntity.ok(ResponseEntity.accepted())
    }

}