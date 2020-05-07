package io.bahlsenwitz.springer.controller.game.menu.create

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.ConfigState
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameChallenge(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val influx: Influx = Influx()
    private val configState: ConfigState = ConfigState()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun challenge(requestChallenge: RequestCreate): ResponseEntity<Any> {
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(requestChallenge.id_self)!!).get()
        val playerOther: Player = repositoryPlayer.findById(UUID.fromString(requestChallenge.id_other)!!).get()
        val config: List<List<String>> = configState.get(requestChallenge.config, playerSelf)
        val game = Game(
            white = playerOther,
            black = playerSelf,
            state = config
        )
        repositoryGame.save(game)
        playerOther.note = true
        repositoryPlayer.save(playerOther)

        influx.game(game, "challenge")
        rating.update(playerSelf, RESULT.WIN)
        return ResponseEntity.accepted().build()
    }

}