package io.bahlsenwitz.springer.generator.test

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.generator.util.GeneratorAvatar
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.util.*

class GeneratorTestData(
    private val repositoryPlayer: RepositoryPlayer,
    private val repositoryGame: RepositoryGame
) {

    private val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun defaultData() {
        this.cleanCollections()



        /**
         * GAME
         **/

        val testGameId: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")!!
        val testGame = Game(
            id = testGameId,
            white = playerWhite,
            black = playerBlack)
        repositoryGame.save(testGame)

        val game00 = Game(
            white = playerA,
            black = playerB,
            status = STATUS.RESOLVED,
            winner = CONTESTANT.WHITE,
            date_end = DATE_TIME_GENERATOR.rightNowString(),
            white_disp = 1,
            black_disp = -7
        )
        repositoryGame.save(game00)

        Thread.sleep(1_000)

        val game01 = Game(
            white = playerA,
            black = playerC,
            status = STATUS.RESOLVED,
            winner = CONTESTANT.WHITE,
            date_end = DATE_TIME_GENERATOR.rightNowString(),
            white_disp = -3,
            black_disp = 1
        )
        repositoryGame.save(game01)

        Thread.sleep(1_000)

        val game02 = Game(
            white = playerA,
            black = playerD,
            status = STATUS.RESOLVED,
            winner = CONTESTANT.BLACK,
            date_end = DATE_TIME_GENERATOR.rightNowString(),
            white_disp = -9,
            black_disp = 0
        )
        repositoryGame.save(game02)

        Thread.sleep(1_000)

        val game03 = Game(
            white = playerA,
            black = playerE,
            status = STATUS.RESOLVED,
            winner = CONTESTANT.NA,
            date_end = DATE_TIME_GENERATOR.rightNowString(),
            white_disp = 14,
            black_disp = -4
        )
        repositoryGame.save(game03)

        val game04 = Game(
            white = playerA,
            black = playerF,
            status = STATUS.RESOLVED,
            winner = CONTESTANT.BLACK,
            date_end = DATE_TIME_GENERATOR.rightNowString(),
            white_disp = 6,
            black_disp = -2
        )
        repositoryGame.save(game04)

        val invite00 = Game(
            white = player7,
            black = playerA,
            status = STATUS.PROPOSED,
            challenger = CONTESTANT.BLACK,
            date_create = DATE_TIME_GENERATOR.rightNowString())
        repositoryGame.save(invite00)

        val invite01 = Game(
            white = playerA,
            black = playerC,
            status = STATUS.PROPOSED,
            challenger = CONTESTANT.BLACK,
            date_create = DATE_TIME_GENERATOR.rightNowString())
        repositoryGame.save(invite01)

        val invite02 = Game(
            white = playerA,
            black = player8,
            status = STATUS.ONGOING,
            challenger = CONTESTANT.BLACK,
            date_create = DATE_TIME_GENERATOR.rightNowString(),
            date_update = DATE_TIME_GENERATOR.rightNowString(),
            turn = CONTESTANT.BLACK)
        repositoryGame.save(invite02)

        val invite03 = Game(
            white = player9,
            black = playerA,
            status = STATUS.ONGOING,
            challenger = CONTESTANT.BLACK,
            date_create = DATE_TIME_GENERATOR.rightNowString(),
            date_update = DATE_TIME_GENERATOR.rightNowString(),
            turn = CONTESTANT.BLACK)
        repositoryGame.save(invite03)

    }

    fun defaultState(): List<List<String>> {
        val row0: List<String> =
            arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
        val row1: List<String> = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
        val row2: List<String> = arrayListOf("", "", "", "", "", "", "", "")
        val row3: List<String> = arrayListOf("", "", "", "", "", "", "", "")
        val row4: List<String> = arrayListOf("", "", "", "", "", "", "", "")
        val row5: List<String> = arrayListOf("", "", "", "", "", "", "", "")
        val row6: List<String> = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
        val row7: List<String> =
            arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
        return arrayListOf(row0, row1, row2, row3, row4, row5, row6, row7)
    }

    private fun cleanCollections() {
        repositoryPlayer.deleteAll()
        repositoryGame.deleteAll()
    }
}