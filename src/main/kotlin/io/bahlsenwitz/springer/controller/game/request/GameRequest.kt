package io.bahlsenwitz.springer.controller.game.request

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import org.springframework.http.ResponseEntity
import java.util.*

class GameRequest(private val repositoryGame: RepositoryGame) {

    fun request(id_game: String): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(id_game)
        val game: Game = repositoryGame.findById(uuid).get()
        return ResponseEntity.ok(game)
    }

}