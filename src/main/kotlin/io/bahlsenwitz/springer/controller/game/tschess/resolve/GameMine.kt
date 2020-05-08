package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.ConfigState
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Output
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameMine(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val dateTime: DateTime = DateTime()
    private val configState: ConfigState = ConfigState()
    private val output: Output = Output(repositoryGame = repositoryGame)
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    data class UpdateMine(val id_game: String, val state: List<List<String>>)

    fun mine(updateMine: UpdateMine): ResponseEntity<Any>? {
        val game: Game = repositoryGame.findById(UUID.fromString(updateMine.id_game)!!).get()
        if (game.status == STATUS.RESOLVED) {
            return null
        }
        game.highlight = "TBD"
        game.status = STATUS.RESOLVED
        game.updated = dateTime.getDate()
        game.condition = CONDITION.LANDMINE
        game.state = configState.poisonReveal(updateMine.state)
        rating.resolve(game)
        return output.terminal(result = "success", route = "mine")
    }

}