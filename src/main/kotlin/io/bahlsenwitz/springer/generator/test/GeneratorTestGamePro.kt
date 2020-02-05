package io.bahlsenwitz.springer.generator.test

import io.bahlsenwitz.springer.model.common.SKIN
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import java.util.*

class GeneratorTestGamePro(
    private val repositoryGame: RepositoryGame,
    private val generatorTestPlayer: GeneratorTestPlayer
) {

    fun generate() {

        val ack = Game(
            id = UUID.fromString("99999999-9999-9999-9999-999999999999")!!,
            white = generatorTestPlayer.findByName(username = "999"),
            black = generatorTestPlayer.findByName(username = "aaa"),
            black_skin = SKIN.DEFAULT,
            challenger = CONTESTANT.BLACK,
            state = traditionalConfig()
        )
        repositoryGame.save(ack)

        val invite00 = Game(
            white = generatorTestPlayer.findByName(username = "777"),
            black = generatorTestPlayer.findByName(username = "aaa"),
            status = STATUS.PROPOSED,
            challenger = CONTESTANT.BLACK,
            state = traditionalConfig()
        )
        repositoryGame.save(invite00)

        val invite01 = Game(
            white = generatorTestPlayer.findByName(username = "aaa"),
            black = generatorTestPlayer.findByName(username = "ccc"),
            status = STATUS.PROPOSED,
            challenger = CONTESTANT.BLACK,
            state = traditionalConfig()
        )
        repositoryGame.save(invite01)

        val invite02 = Game(
            white = generatorTestPlayer.findByName(username = "aaa"),
            black = generatorTestPlayer.findByName(username = "888"),
            status = STATUS.ONGOING,
            challenger = CONTESTANT.BLACK,
            turn = CONTESTANT.BLACK,
            state = traditionalConfig()
        )
        repositoryGame.save(invite02)

        val invite03 = Game(
            white = generatorTestPlayer.findByName(username = "999"),
            black = generatorTestPlayer.findByName(username = "aaa"),
            status = STATUS.ONGOING,
            challenger = CONTESTANT.BLACK,
            turn = CONTESTANT.BLACK,
            state = traditionalConfig()
        )
        repositoryGame.save(invite03)
    }

    private fun traditionalConfig(): List<List<String>> {
        val r1 = arrayListOf(
            "PawnBlack",
            "PawnBlack",
            "PawnBlack",
            "PawnBlack",
            "PawnBlack",
            "PawnBlack",
            "PawnBlack",
            "PawnBlack"
        )
        val r0 = arrayListOf(
            "RookBlack",
            "KnightBlack",
            "BishopBlack",
            "QueenBlack",
            "KingBlack",
            "BishopBlack",
            "KnightBlack",
            "RookBlack"
        )
        val re = arrayListOf("", "", "", "", "", "", "", "")
        return arrayListOf(r0, r1, re, re, re, re, re, re)
    }
}