package io.bahlsenwitz.springer.controller.game.tschess

import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.DateTime
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
            game.updated = DateTime().getDate()
        }
        if(requestTest.turn == "WHITE"){
            game.turn = CONTESTANT.WHITE
        }
        if(requestTest.turn == "BLACK"){
            game.turn = CONTESTANT.BLACK
        }
        game.winner = null
        game.status = STATUS.ONGOING
        game.on_check = false
        game.condition = CONDITION.TBD
        game.highlight = "TBD"
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    data class RequestTest(
        val state: List<List<String>>,
        val turn: String
    )

    private fun defaultState(): List<List<String>> {
        val row0: List<String> =
            arrayListOf(
                "RookWhite_x",
                "KnightWhite_x",
                "BishopWhite_x",
                "QueenWhite_x",
                "KingWhite_x",
                "BishopWhite_x",
                "KnightWhite_x",
                "RookWhite_x"
            )
        val row1: List<String> = arrayListOf(
            "PawnWhite_x",
            "PawnWhite_x",
            "PawnWhite_x",
            "PawnWhite_x",
            "PawnWhite_x",
            "PawnWhite_x",
            "PawnWhite_x",
            "PawnWhite_x"
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