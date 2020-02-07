package io.bahlsenwitz.springer.controller.game.test

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

class GameTest(private val repositoryGame: RepositoryGame) {

    fun requestTest(requestTest: RequestTest): ResponseEntity<Any> {
        val id: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val game: Game = repositoryGame.findById(id).get()
        if(requestTest.state != null){
            game.state = requestTest.state
        }
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    data class RequestTest(
        //val id_test: String,
        val state: List<List<String>>? = null
    )
}