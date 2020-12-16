package io.bahlsenwitz.springer.controller.game.path

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameMate(
    private val repositoryGame: RepositoryGame,
    repositoryPlayer: RepositoryPlayer
) {
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun mate(id_game: String): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(id_game)!!).get()
        game.highlight = "9999"
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.CHECKMATE
        game.updated = DateTime().getDate()
        //repositoryGame.save(game)
        rating.resolve(game) //TODO: contains "repositoryGame.save(game)"

        val body: MutableMap<String, String> = HashMap()
        body["success"] = "mate"
        return ResponseEntity.ok().body(body)
    }
}