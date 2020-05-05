package io.bahlsenwitz.springer.controller.game.menu.invite

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game

import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.ChessConfig
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.util.*

class GameAck(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    data class RequestAck(
        val id_self: String,
        val id_other: String,
        val config: Int, //0, 1, 2, 3
        val id_game: String
    )

    fun ack(requestAck: RequestAck): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(requestAck.id_game)!!).get()
        var white: Boolean = true
        if (game.challenger == CONTESTANT.WHITE) {
            white = false
        } //the ACK'r plays as white...

        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(requestAck.id_self)!!).get()
        val playerOther: Player = repositoryPlayer.findById(UUID.fromString(requestAck.id_other)!!).get()

        var config: List<List<String>> = ChessConfig().getConfigChess()
        if (requestAck.config == 0) {
            config = playerSelf.config0
        }
        if (requestAck.config == 1) {
            config = playerSelf.config1
        }
        if (requestAck.config == 2) {
            config = playerSelf.config2
        }
        val state: List<List<String>> = generateState(config, game.state!!, white)
        game.state = state
        game.status = STATUS.ONGOING
        game.updated = DateTime().getDate()

        setNotification(game, playerOther, repositoryPlayer)
        Influx().game(game.id.toString(), "ack")
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    companion object {

        fun setNotification(game: Game, player: Player, repositoryPlayer: RepositoryPlayer) {
            if (game.white == player) {
                game.black.note = true
                repositoryPlayer.save(game.black)
                return
            }
            game.white.note = true
            repositoryPlayer.save(game.white)
        }

        private fun setOrientation(row: List<String>, color: String): List<String> {
            val colorRow: MutableList<String> = mutableListOf()
            for (element: String in row) {
                if (element == "") {
                    colorRow.add(element)
                    continue
                }
                colorRow.add("${element}${color}_x")
            }
            return colorRow
        }

        fun generateState(config: List<List<String>>, state: List<List<String>>, white: Boolean): List<List<String>> {
            val row: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val rowWhite00: List<String> = setOrientation(row = config[0], color = "White")
            val rowWhite01: List<String> = setOrientation(row = config[1], color = "White")
            /* * */
            val rowBlack01: List<String> = setOrientation(row = state[1], color = "Black")
            val rowBlack00: List<String> = setOrientation(row = state[0], color = "Black")
            return arrayListOf(rowBlack00, rowBlack01, row, row, row, row, rowWhite01, rowWhite00)
        }
    }
}