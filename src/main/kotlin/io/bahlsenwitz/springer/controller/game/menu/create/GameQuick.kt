package io.bahlsenwitz.springer.controller.game.menu.create

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
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

class GameQuick(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()
    private val configState: ConfigState = ConfigState()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun quick(requestQuick: RequestCreate): ResponseEntity<Any> {
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(requestQuick.id_self)!!).get()
        playerSelf.date = dateTime.getDate()
        rating.update(playerSelf, RESULT.WIN)

        val playerOther: Player = repositoryPlayer.findById(UUID.fromString(requestQuick.id_other)!!).get()
        playerOther.note = true
        repositoryPlayer.save(playerOther)

        val state: List<List<String>> = generateState(configState.get(requestQuick.config, playerSelf))

        val game = Game(
            state = state,
            white = playerSelf,
            black = playerOther,
            challenger = CONTESTANT.WHITE,
            status = STATUS.ONGOING
        )
        influx.game(game, "quick")
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    private fun generateState(white: List<List<String>>): List<List<String>> {
        val color: String = "White"
        val row00: List<String> = configState.orient(white[0], color)
        val row01: List<String> = configState.orient(white[1], color)
        val black: List<List<String>> = configState.quickBlack()
        return arrayListOf(row00, row01, configState.row, configState.row, configState.row, configState.row, black[0], black[1])
    }
}