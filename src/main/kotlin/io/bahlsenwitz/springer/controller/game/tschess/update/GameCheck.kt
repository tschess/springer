package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.util.*

//private val repositoryPlayer: RepositoryPlayer <-- gotta notifyu these guys...
class GameCheck(private val repositoryGame: RepositoryGame) {

    fun check(id_game: String): ResponseEntity<Any> {
        val uuid0: UUID = UUID.fromString(id_game)!!
        Influx().game(game_id = uuid0.toString(), route = "check")

        val game: Game = repositoryGame.findById(uuid0).get()
        //khttp.post(url = "${DateTime().INFLUX}write?db=tschess", data = "game id=\"${game.id}\",route=\"check\"")

        if(game.on_check){
            return ResponseEntity.ok("{\"success\": \"ok\"}")
        }
        game.on_check = true
        game.updated = DateTime().getDate()
        repositoryGame.save(game)
        return ResponseEntity.ok("{\"success\": \"ok\"}") //what does this need to return? the game I guess...
    }

}