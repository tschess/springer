package io.bahlsenwitz.springer.generator.test

import io.bahlsenwitz.springer.model.game.*
import io.bahlsenwitz.springer.repository.RepositoryGame
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class GeneratorTestGameFin(
    private val repositoryGame: RepositoryGame,
    private val generatorTestPlayer: GeneratorTestPlayer
) {

    val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    fun generate() {
        repositoryGame.deleteAll()

        val snap = Game(
            id = UUID.fromString("11111111-1111-1111-1111-111111111111")!!,
            white = generatorTestPlayer.findByName(username = "999"),
            black = generatorTestPlayer.findByName(username = "888"),
            state = defaultState(),
            moves = 33,
            white_disp = 1,
            white_skin = SKIN.CALYPSO,
            black_skin = SKIN.HYPERION,
            black_disp = -7,
            outcome = OUTCOME.CHECKMATE,
            winner = CONTESTANT.WHITE,
            updated = FORMATTER.format(ZonedDateTime.now(Game.BROOKLYN)).toString(),
            status = STATUS.RESOLVED,
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(snap)

        val game00 = Game(
            white = generatorTestPlayer.findByName(username = "aaa"),
            black = generatorTestPlayer.findByName(username = "bbb"),
            status = STATUS.RESOLVED,
            winner = CONTESTANT.WHITE,
            updated = FORMATTER.format(ZonedDateTime.now(Game.BROOKLYN)).toString(),
            white_disp = 1,
            white_skin = SKIN.CALYPSO,
            black_skin = SKIN.NEPTUNE,
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
            updated = FORMATTER.format(ZonedDateTime.now(Game.BROOKLYN)).toString(),
            white_disp = -3,
            black_disp = 1,
            white_skin = SKIN.CALYPSO,
            black_skin = SKIN.IAPETUS,
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
            updated = FORMATTER.format(ZonedDateTime.now(Game.BROOKLYN)).toString(),
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
            updated = FORMATTER.format(ZonedDateTime.now(Game.BROOKLYN)).toString(),
            white_disp = 14,
            black_disp = -4,
            white_skin = SKIN.CALYPSO,
            black_skin = SKIN.DEFAULT,
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
            updated = FORMATTER.format(ZonedDateTime.now(Game.BROOKLYN)).toString(),
            white_disp = 6,
            black_disp = -2,
            white_skin = SKIN.CALYPSO,
            black_skin = SKIN.NEPTUNE,
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