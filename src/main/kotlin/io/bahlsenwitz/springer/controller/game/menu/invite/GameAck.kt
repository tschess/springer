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
        val id_game: String,
        val config: Int
    )

    fun ack(requestAck: RequestAck): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(requestAck.id_game)!!).get()
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(requestAck.id_self)!!).get()
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
        val state: List<List<String>> = generateState(config, game.state!!)
        game.state = state
        game.status = STATUS.ONGOING
        game.updated = DateTime().getDate()
        setNotification(game, playerSelf, repositoryPlayer)
        Influx().game(game.id.toString(), "ack")
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    companion object {

        fun setNotification(game: Game, playerSelf: Player, repositoryPlayer: RepositoryPlayer) {
            if (game.white == playerSelf) {
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

        fun generateState(config: List<List<String>>, state: List<List<String>>): List<List<String>> {
            val row: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val white00: List<String> = setOrientation(row = config[0], color = "White")
            val white01: List<String> = setOrientation(row = config[1], color = "White")
            val black00: List<String> = setOrientation(row = state[0], color = "Black")
            val black01: List<String> = setOrientation(row = state[1], color = "Black")
            return arrayListOf(white00, white01, row, row, row, row, black01, black00)
        }
    }
}