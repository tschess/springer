package io.bahlsenwitz.springer.controller.game.path

import io.bahlsenwitz.springer.repository.RepositoryGame
import org.springframework.http.ResponseEntity
import java.util.*

class GameRequest(private val repositoryGame: RepositoryGame) {

    fun request(id_game: String): ResponseEntity<Any> {
        return ResponseEntity.ok(repositoryGame.findById(UUID.fromString(id_game)).get())
    }

}