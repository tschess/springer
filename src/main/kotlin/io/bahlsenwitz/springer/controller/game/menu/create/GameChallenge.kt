package io.bahlsenwitz.springer.controller.game.menu.create

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Config
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameChallenge(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val config: Config = Config()
    private val influx: Influx = Influx()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    data class RequestChallenge(
        val id_self: String,
        val id_other: String,
        val config: Int
    )

    fun challenge(requestChallenge: RequestChallenge): ResponseEntity<Any> {
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(requestChallenge.id_self)!!).get()
        val playerOther: Player = repositoryPlayer.findById(UUID.fromString(requestChallenge.id_other)!!).get()
        val config: List<List<String>> = config.get(requestChallenge.config, playerSelf)
        val game = Game(
            white = playerOther,
            black = playerSelf,
            challenger = CONTESTANT.BLACK,
            state = config
        )
        repositoryGame.save(game)
        playerOther.note = true
        repositoryPlayer.save(playerOther)

        influx.game(game, "challenge")
        rating.update(playerSelf, RESULT.WIN)
        return ResponseEntity.ok(ResponseEntity.accepted())
    }

}