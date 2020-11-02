package io.bahlsenwitz.springer.controller.game.request

import io.bahlsenwitz.springer.model.rating.RESULT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Config
import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.push.Pusher
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameChallenge(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    data class RequestCreate(
        val id_self: String,
        val id_other: String,
        val config: Int
    )

    private val output: Output = Output()
    private val config: Config = Config()
    private val pusher: Pusher = Pusher()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun challenge(requestChallenge: RequestCreate): ResponseEntity<Any> {
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(requestChallenge.id_self)!!).get()
        val playerOther: Player = repositoryPlayer.findById(UUID.fromString(requestChallenge.id_other)!!).get()
        val config: List<List<String>> = config.get(requestChallenge.config, playerSelf)
        val game = Game(
            white = playerOther,
            black = playerSelf,
            state = config
        )
        repositoryGame.save(game)
        /* * */
        pusher.notify(playerOther)
        /* * */
        repositoryPlayer.save(playerOther)
        rating.update(playerSelf, RESULT.ACTION)
        return output.terminal(result = "success", route = "challenge")
    }

}