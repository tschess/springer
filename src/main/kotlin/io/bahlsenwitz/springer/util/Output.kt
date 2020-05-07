package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import org.springframework.http.ResponseEntity
import java.util.HashMap

class Output {

    private val influx: Influx = Influx()
    private var body: MutableMap<String, String> = HashMap()

    fun success(route: String, game: Game? = null, player: Player? = null): ResponseEntity<Any> {
        if(game != null){
            influx.game(game, route)
        }
        if(player != null){
            influx.activity(player, route)
        }
        body["response"] = "success"
        return ResponseEntity.accepted().body(body)
    }

    fun unassigned(): ResponseEntity<Any> {
        body["response"] = "unassigned"
        return ResponseEntity.accepted().body(body)
    }
}