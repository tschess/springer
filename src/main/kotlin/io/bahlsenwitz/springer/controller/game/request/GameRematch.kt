package io.bahlsenwitz.springer.controller.game.request

import io.bahlsenwitz.springer.model.rating.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Config
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.push.Pusher
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameRematch(private val repositoryGame: RepositoryGame, private val repositoryPlayer: RepositoryPlayer ) {

    private val config: Config = Config()
    private val pusher: Pusher = Pusher()
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
        /* * */
        pusher.notify(playerOther)
        /* * */
        val config: List<List<String>> = config.get(requestRematch.config, playerSelf)
        val white: Boolean = requestRematch.white
        val game: Game = Game(white = playerSelf, black = playerOther, challenger = CONTESTANT.WHITE, state = config)
        if (!white) {
            game.white = playerOther
            game.black = playerSelf
            game.challenger = CONTESTANT.BLACK
        }
        rating.update(playerSelf, RESULT.WIN)
        //return output.update(route = "rematch", game = game)
        game.updated = dateTime.getDate()
        repositoryGame.save(game)

        val body: MutableMap<String, String> = HashMap()
        body["success"] = "rematch"
        return ResponseEntity.ok().body(body)
    }
}