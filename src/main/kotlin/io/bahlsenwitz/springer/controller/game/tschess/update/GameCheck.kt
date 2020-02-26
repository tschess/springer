package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.OUTCOME
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.Constant
import org.springframework.http.ResponseEntity
import java.util.*

//private val repositoryPlayer: RepositoryPlayer <-- gotta notifyu these guys...
class GameCheck(private val repositoryGame: RepositoryGame) {

    fun check(id_game: String): ResponseEntity<Any> {
        val uuid0: UUID = UUID.fromString(id_game)!!
        val game: Game = repositoryGame.findById(uuid0).get()

        game.outcome = OUTCOME.CHECK
        game.updated = Constant().getDate()
        repositoryGame.save(game)

        khttp.post(url = "${Constant().INFLUX}write?db=tschess", data = "game id=\"${game.id}\",route=\"check\"")
        return ResponseEntity.ok("{\"success\": \"ok\"}") //what does this need to return? the game I guess...
    }

}