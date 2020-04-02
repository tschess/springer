package io.bahlsenwitz.springer.controller.game.menu.invite

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.Game

import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.util.*

class GameAck(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun ack(requestAck: RequestAck): ResponseEntity<Any> {

        val uuid0: UUID = UUID.fromString(requestAck.id_game)!!
        val game: Game = repositoryGame.findById(uuid0).get()

        val uuid1: UUID = UUID.fromString(requestAck.id_player)!!
        val player: Player = repositoryPlayer.findById(uuid1).get()

        //khttp.post(url = "${DateTime().INFLUX}write?db=tschess", data = "game id=\"${game.id}\",route=\"ack\"")
        Influx().game(game_id = game.id.toString(), route = "ack")

        var config: List<List<String>> =
            traditionalConfig()
        if (requestAck.index == 0) {
            config = player.config0
        }
        if (requestAck.index == 1) {
            config = player.config1
        }
        if (requestAck.index == 2) {
            config = player.config2
        }
        val state: List<List<String>> =
            generateState(
                config,
                game.state!!
            )

        game.state = state
        game.status = STATUS.ONGOING
        game.updated = DateTime().getDate()



        setNotification(
            game = game,
            player = player,
            repositoryPlayer = repositoryPlayer
        )
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    data class RequestAck(
        val id_game: String,
        val id_player: String,
        val skin: String,
        val index: Int //0, 1, 2, 3
    )

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

        fun traditionalConfig(): List<List<String>> {
            val r0 = arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
            val r1 = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
            return arrayListOf(r0, r1)
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
            return arrayListOf(
                setOrientation(
                    row = config[0],
                    color = "White"
                ),
                setOrientation(
                    row = config[1],
                    color = "White"
                ),
                arrayListOf("", "", "", "", "", "", "", ""),
                arrayListOf("", "", "", "", "", "", "", ""),
                arrayListOf("", "", "", "", "", "", "", ""),
                arrayListOf("", "", "", "", "", "", "", ""),
                setOrientation(
                    row = state[1],
                    color = "Black"
                ),
                setOrientation(
                    row = state[0],
                    color = "Black"
                )
            )
        }
    }
}