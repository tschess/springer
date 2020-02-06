package io.bahlsenwitz.springer.generator.test

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.OUTCOME
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import java.util.*

class GeneratorTestGameFin(
    private val repositoryGame: RepositoryGame,
    private val generatorTestPlayer: GeneratorTestPlayer
) {

    private val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun generate() {
        repositoryGame.deleteAll()

        val snap = Game(
            id = UUID.fromString("11111111-1111-1111-1111-111111111111")!!,
            white = generatorTestPlayer.findByName(username = "999"),
            black = generatorTestPlayer.findByName(username = "888"),
            state = defaultState(),
            moves = 33,
            white_disp = 1,
            black_disp = -7,
            outcome = OUTCOME.CHECKMATE,
            winner = CONTESTANT.WHITE,
            updated = DATE_TIME_GENERATOR.rightNowString(),
            status = STATUS.RESOLVED,
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(snap)

        val game00 = Game(
            white = generatorTestPlayer.findByName(username = "aaa"),
            black = generatorTestPlayer.findByName(username = "bbb"),
            status = STATUS.RESOLVED,
            winner = CONTESTANT.WHITE,
            updated = DATE_TIME_GENERATOR.rightNowString(),
            white_disp = 1,
            black_disp = -7,
            state = defaultState(),
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(game00)

        Thread.sleep(1_000)

        val game01 = Game(
            white = generatorTestPlayer.findByName(username = "aaa"),
            black = generatorTestPlayer.findByName(username = "ccc"),
            status = STATUS.RESOLVED,
            winner = CONTESTANT.WHITE,
            updated = DATE_TIME_GENERATOR.rightNowString(),
            white_disp = -3,
            black_disp = 1,
            state = defaultState(),
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(game01)

        Thread.sleep(1_000)

        val game02 = Game(
            white = generatorTestPlayer.findByName(username = "aaa"),
            black = generatorTestPlayer.findByName(username = "ddd"),
            status = STATUS.RESOLVED,
            winner = CONTESTANT.BLACK,
            updated = DATE_TIME_GENERATOR.rightNowString(),
            white_disp = -9,
            black_disp = 0,
            state = defaultState(),
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(game02)

        Thread.sleep(1_000)

        val game03 = Game(
            white = generatorTestPlayer.findByName(username = "aaa"),
            black = generatorTestPlayer.findByName(username = "eee"),
            status = STATUS.RESOLVED,
            updated = DATE_TIME_GENERATOR.rightNowString(),
            white_disp = 14,
            black_disp = -4,
            state = defaultState(),
            challenger = CONTESTANT.WHITE,
            winner = CONTESTANT.WHITE
        )
        repositoryGame.save(game03)

        val game04 = Game(
            white = generatorTestPlayer.findByName(username = "aaa"),
            black = generatorTestPlayer.findByName(username = "fff"),
            status = STATUS.RESOLVED,
            winner = CONTESTANT.BLACK,
            updated = DATE_TIME_GENERATOR.rightNowString(),
            white_disp = 6,
            black_disp = -2,
            state = defaultState(),
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(game04)
    }

    private fun defaultState(): List<List<String>> {
        val row0: List<String> =
            arrayListOf(
                "RookWhite",
                "KnightWhite",
                "BishopWhite",
                "QueenWhite",
                "KingWhite",
                "BishopWhite",
                "KnightWhite",
                "RookWhite"
            )
        val row1: List<String> = arrayListOf(
            "PawnWhite",
            "PawnWhite",
            "PawnWhite",
            "PawnWhite",
            "PawnWhite",
            "PawnWhite",
            "PawnWhite",
            "PawnWhite"
        )
        val row2: List<String> = arrayListOf("", "", "", "", "", "", "", "")
        val row3: List<String> = arrayListOf("", "", "", "", "", "", "", "")
        val row4: List<String> = arrayListOf("", "", "", "", "", "", "", "")
        val row5: List<String> = arrayListOf("", "", "", "", "", "", "", "")
        val row6: List<String> = arrayListOf(
            "PawnBlack",
            "PawnBlack",
            "PawnBlack",
            "PawnBlack",
            "PawnBlack",
            "PawnBlack",
            "PawnBlack",
            "PawnBlack"
        )
        val row7: List<String> =
            arrayListOf(
                "RookBlack",
                "KnightBlack",
                "BishopBlack",
                "QueenBlack",
                "KingBlack",
                "BishopBlack",
                "KnightBlack",
                "RookBlack"
            )
        return arrayListOf(row0, row1, row2, row3, row4, row5, row6, row7)
    }
}