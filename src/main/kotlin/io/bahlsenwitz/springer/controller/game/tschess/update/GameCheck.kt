package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.OUTCOME
import io.bahlsenwitz.springer.repository.RepositoryGame
import org.springframework.http.ResponseEntity
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

//private val repositoryPlayer: RepositoryPlayer <-- gotta notifyu these guys...
class GameCheck(private val repositoryGame: RepositoryGame) {

    fun check(id_game: String): ResponseEntity<Any> {
        val uuid0: UUID = UUID.fromString(id_game)!!
        val game: Game = repositoryGame.findById(uuid0).get()

        game.outcome = OUTCOME.CHECK
        game.updated = Date.from(ZonedDateTime.now(ZoneId.of("America/New_York")).toInstant())
        repositoryGame.save(game)
        return ResponseEntity.ok("{\"success\": \"ok\"}") //what does this need to return? the game I guess...
    }

}