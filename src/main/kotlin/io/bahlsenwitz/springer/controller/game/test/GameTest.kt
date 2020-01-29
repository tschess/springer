package io.bahlsenwitz.springer.controller.game.test

import io.bahlsenwitz.springer.model.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import java.util.*
import javax.validation.Valid

class GameTest(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun updateTest(updateTest: UpdateTest): ResponseEntity<Any> {

        val id: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val game: Game = repositoryGame.findById(id).get()

        //if (requestTest.state != null) {
        //game.state = requestTest.state
        //}
        //game.check_on = requestTest.check_on
        //game.catalyst = requestTest.catalyst
        //game.winner = requestTest.winner
        //game.black_update = requestTest.updated
        //game.updated = requestTest.updated
        //game.created = requestTest.created
        //game.status = "ONGOING"
        return ResponseEntity.status(HttpStatus.OK).body("{\"game\": \"${updateTest.test}\"}")
    }

    data class UpdateTest(
        val test: String
    )

    fun requestTest(requestTest: RequestTest): ResponseEntity<Any> {
        //if (requestTest.state != null) {
        //game.state = requestTest.state
        //}
        //game.check_on = requestTest.check_on
        //game.catalyst = requestTest.catalyst
        //game.winner = requestTest.winner
        //game.black_update = requestTest.updated
        //game.updated = requestTest.updated
        //game.created = requestTest.created
        //game.status = "ONGOING"
        val id: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val game: Game = repositoryGame.findById(id).get()
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    data class RequestTest(
        val test: String
        //val state: List<List<String>>,
        //val catalyst: String,
        //val check_on: String,
        //val winner: String,
        //val updated: String,
        //val created: String
    )
}