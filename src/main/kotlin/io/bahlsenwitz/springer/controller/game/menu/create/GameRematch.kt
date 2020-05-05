package io.bahlsenwitz.springer.controller.game.menu.create

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Config
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameRematch(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val config: Config = Config()
    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    data class RequestRematch(
        val id_self: String,
        val id_other: String,
        val config: Int,
        val white: Boolean
    )

    fun rematch(requestRematch: RequestRematch): ResponseEntity<Any> {
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(requestRematch.id_self)!!).get()
        playerSelf.date = dateTime.getDate()
        val playerOther: Player = repositoryPlayer.findById(UUID.fromString(requestRematch.id_other)!!).get()
        val config: List<List<String>> = config.get(requestRematch.config, playerSelf)

        val white: Boolean = requestRematch.white
        val game: Game = Game(white = playerSelf, black = playerOther, challenger = CONTESTANT.WHITE, state = config)
        if (!white) {
            game.white = playerOther
            game.black = playerSelf
            game.challenger = CONTESTANT.BLACK
        }
        repositoryGame.save(game)
        rating.update(playerSelf, RESULT.WIN)
        influx.game(game, "rematch")
        return ResponseEntity.ok(ResponseEntity.accepted())
    }
}