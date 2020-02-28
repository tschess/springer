package io.bahlsenwitz.springer.controller.game.tschess

import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.Constant
import org.springframework.http.ResponseEntity
import java.util.*

//curl --header "Content-Type: application/json" --request POST --data '{"state":[[""]]}' http://localhost:8080/game/test
class GameTest(private val repositoryGame: RepositoryGame) {

    fun test(requestTest: RequestTest): ResponseEntity<Any> {
        val id: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val game: Game = repositoryGame.findById(id).get()
        if(requestTest.state == arrayListOf(arrayListOf("DEFAULT"))){
            game.state = defaultState()
        }
        else if(requestTest.state != arrayListOf(arrayListOf(""))){
            game.state = requestTest.state
        }
        if(requestTest.turn == "WHITE"){
            game.turn = CONTESTANT.WHITE
        }
        if(requestTest.turn == "BLACK"){
            game.turn = CONTESTANT.BLACK
        }
        game.winner = null
        game.status = STATUS.ONGOING
        game.condition = CONDITION.TBD
        game.highlight = "TBD"
        game.updated = Constant().getDate()
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    data class RequestTest(
        val state: List<List<String>>,
        val turn: String
    )

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