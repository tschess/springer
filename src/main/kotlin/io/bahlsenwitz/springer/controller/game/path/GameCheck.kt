package io.bahlsenwitz.springer.controller.game.path

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.util.*

class GameCheck(private val repositoryGame: RepositoryGame) {

    fun check(id_game: String): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(id_game)!!).get()
        if (game.on_check) {
            val body: MutableMap<String, String> = HashMap()
            body["fail"] = "check"
            return ResponseEntity.ok().body(body)
        }
        game.on_check = true
        game.updated = DateTime().getDate()
        repositoryGame.save(game)
        val body: MutableMap<String, String> = HashMap()
        body["success"] = "check"
        return ResponseEntity.ok().body(body)
    }

}