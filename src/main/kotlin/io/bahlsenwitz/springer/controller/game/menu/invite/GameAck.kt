package io.bahlsenwitz.springer.controller.game.menu.invite

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.common.RESULT
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

class GameAck(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val configState: ConfigState = ConfigState()
    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    data class RequestAck(
        val id_self: String,
        val id_game: String,
        val config: Int
    )

    fun ack(requestAck: RequestAck): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(requestAck.id_game)!!).get()
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(requestAck.id_self)!!).get()
        playerSelf.date = dateTime.getDate()
        rating.update(playerSelf, RESULT.WIN)
        setNotification(game, playerSelf, repositoryPlayer)

        val config: List<List<String>> = configState.get(requestAck.config, playerSelf)
        val state: List<List<String>> = generateState(config, game.state!!)
        game.state = state
        game.status = STATUS.ONGOING
        game.updated = DateTime().getDate()

        influx.game(game, "ack")
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    private fun generateState(config: List<List<String>>, state: List<List<String>>): List<List<String>> {
        val row: List<String> = arrayListOf("", "", "", "", "", "", "", "")
        val white00: List<String> = this.configState.orient(config[0], "White")
        val white01: List<String> = this.configState.orient(config[1], "White")
        val black00: List<String> = this.configState.orient(state[0], "Black")
        val black01: List<String> = this.configState.orient(state[1], "Black")
        return arrayListOf(white00, white01, row, row, row, row, black01, black00)
    }

    private fun setNotification(game: Game, playerSelf: Player, repositoryPlayer: RepositoryPlayer) {
        if (game.white == playerSelf) {
            game.black.note = true
            repositoryPlayer.save(game.black)
            return
        }
        game.white.note = true
        repositoryPlayer.save(game.white)
    }

}