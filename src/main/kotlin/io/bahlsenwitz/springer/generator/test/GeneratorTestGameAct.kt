package io.bahlsenwitz.springer.generator.test

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import java.util.*

class GeneratorTestGameAct(
    private val repositoryGame: RepositoryGame,
    private val generatorTestPlayer: GeneratorTestPlayer
) {

    fun generate() {
        val test = Game(
            id = UUID.fromString("00000000-0000-0000-0000-000000000000")!!,
            white = generatorTestPlayer.findByName(username = "white"),
            black = generatorTestPlayer.findByName(username = "black"),
            state = defaultState()
        )
        repositoryGame.save(test)

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