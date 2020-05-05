package io.bahlsenwitz.springer.controller.game.menu.create

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game

import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Config
import io.bahlsenwitz.springer.util.Rating

import org.springframework.http.ResponseEntity
import java.util.*

class GameQuick(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val config: Config = Config()
    private val influx: Influx = Influx()
    private val rating: Rating = Rating(repositoryPlayer)

    data class RequestQuick(
        val id_self: String,
        val id_other: String,
        val config: Int
    )

    fun quick(requestQuick: RequestQuick): ResponseEntity<Any> {
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(requestQuick.id_self)!!).get()
        rating.update(playerSelf, RESULT.WIN)

        val config: List<List<String>> = config.get(requestQuick.config, playerSelf)
        val state: List<List<String>> = generateState(config)

        val playerOther: Player = repositoryPlayer.findById(UUID.fromString(requestQuick.id_other)!!).get()
        playerOther.note = true
        repositoryPlayer.save(playerOther)

        val game = Game(
            state = state,
            white = playerSelf,
            black = playerOther,
            challenger = CONTESTANT.WHITE,
            status = STATUS.ONGOING
        )

        influx.game(game,"quick")
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    companion object {

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

    }
}