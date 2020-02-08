package io.bahlsenwitz.springer.controller.game.test

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import org.springframework.http.ResponseEntity
import java.util.*

//curl --header "Content-Type: application/json" --request POST --data '{"state":[[""]]}' http://localhost:8080/game/test
class GameTest(private val repositoryGame: RepositoryGame) {

    private val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun test(requestTest: RequestTest): ResponseEntity<Any> {
        val id: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val game: Game = repositoryGame.findById(id).get()
        if(requestTest.state != arrayListOf(arrayListOf(""))){
            game.state = requestTest.state
        }
        if(requestTest.turn == "WHITE"){
            game.turn = CONTESTANT.WHITE
        }
        if(requestTest.turn == "BLACK"){
            game.turn = CONTESTANT.BLACK
        }
        game.updated = DATE_TIME_GENERATOR.rightNowString()
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    data class RequestTest(
        val state: List<List<String>>,
        val turn: String
    )
}