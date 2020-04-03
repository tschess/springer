package io.bahlsenwitz.springer.controller.game.menu.create

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game

import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer

import org.springframework.http.ResponseEntity
import java.util.*

//curl --header "Content-Type: application/json" --request POST --data '{"game":"11111111-1111-1111-1111-111111111111", "player": "99999999-9999-9999-9999-999999999999"}' http://localhost:8080/game/snapshot
class GameQuick(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun quick(requestQuick: RequestQuick): ResponseEntity<Any> {

        val uuid0: UUID = UUID.fromString(requestQuick.id_self)!!
        val white: Player = repositoryPlayer.findById(uuid0).get() //self

        var config: List<List<String>> =
            traditionalConfig()
        if (requestQuick.config == 0) {
            config = white.config0
        }
        if (requestQuick.config == 1) {
            config = white.config1
        }
        if (requestQuick.config == 2) {
            config = white.config2
        }

        val state: List<List<String>> =
            generateState(config)

        val uuid1: UUID = UUID.fromString(requestQuick.id_other)!!  //other
        val black: Player = repositoryPlayer.findById(uuid1).get()

        black.note = true
        repositoryPlayer.save(black)

        val game = Game(
            state = state,
            white = white,

            black = black,

            challenger = CONTESTANT.WHITE)
        game.status = STATUS.ONGOING

        //khttp.post(url = "${DateTime().INFLUX}write?db=tschess", data = "game id=\"${game.id}\",route=\"quick\"")
        Influx().game(game_id = game.id.toString(), route = "quick")

        return ResponseEntity.ok(repositoryGame.save(game))
    }

    data class RequestQuick(
        val id_self: String, //white
        val id_other: String,
        val config: Int //0, 1, 2, 3
    )

    companion object {

        fun traditionalConfig(): List<List<String>> {
            val row0: List<String> =
                arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
            val row1: List<String> = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
            return arrayListOf(row0, row1)
        }

        fun generateState(config: List<List<String>>): List<List<String>> {
            val row0: List<String> =
                setOrientation(
                    row = config[0],
                    color = "White"
                )
            val row1: List<String> =
                setOrientation(
                    row = config[1],
                    color = "White"
                )
            val row2: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row3: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row4: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row5: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row6: List<String> = arrayListOf(
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x"
            )
            val row7: List<String> =
                arrayListOf(
                    "RookBlack_x",
                    "KnightBlack_x",
                    "BishopBlack_x",
                    "QueenBlack_x",
                    "KingBlack_x",
                    "BishopBlack_x",
                    "KnightBlack_x",
                    "RookBlack_x"
                )
            return arrayListOf(row0, row1, row2, row3, row4, row5, row6, row7)
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
    }
}