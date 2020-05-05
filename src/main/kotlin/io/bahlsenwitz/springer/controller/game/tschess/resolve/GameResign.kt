package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.ConfigState
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameResign(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()
    private val configState: ConfigState = ConfigState()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    data class UpdateResign(
        val id_game: String,
        val id_self: String,
        val white: Boolean
    )

    fun resign(updateResign: UpdateResign): ResponseEntity<Any> {
        val date: String = dateTime.getDate()
        val game: Game = repositoryGame.findById(UUID.fromString(updateResign.id_game)!!).get()
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(updateResign.id_self)!!).get()
        playerSelf.updated = date
        if (game.status == STATUS.RESOLVED) {
            return ResponseEntity.ok(ResponseEntity.accepted())
        }
        game.updated = date
        game.state = configState.poisonReveal(game.state!!)
        game.highlight = "TBD"
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.RESIGN
        rating.resolve(game)
        influx.game(game, "resign")
        return ResponseEntity.ok(ResponseEntity.accepted())
    }
}