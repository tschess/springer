package io.bahlsenwitz.springer.controller.game.check

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.OUTCOME
import io.bahlsenwitz.springer.repository.RepositoryGame
import org.springframework.http.ResponseEntity
import java.util.*

//private val repositoryPlayer: RepositoryPlayer <-- gotta notifyu these guys...
class GameCheck(private val repositoryGame: RepositoryGame) {

    val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun check(id_game: String): ResponseEntity<Any> {
        val uuid0: UUID = UUID.fromString(id_game)!!
        val game: Game = repositoryGame.findById(uuid0).get()

        game.outcome = OUTCOME.CHECK
        game.updated = DATE_TIME_GENERATOR.rightNowString()
        repositoryGame.save(game)
        return ResponseEntity.ok("{\"success\": \"ok\"}") //what does this need to return? the game I guess...
    }

}