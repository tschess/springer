package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class Output(private val repositoryPlayer: RepositoryPlayer, private val repositoryGame: RepositoryGame? = null) {

    private val influx: Influx = Influx()
    private var body: MutableMap<String, String> = HashMap()

    fun success(route: String, game: Game? = null, player: Player? = null): ResponseEntity<Any> {
        if (game != null) {
            influx.game(game, route)
        }
        if (player != null) {
            influx.activity(player, route)
        }
        body["success"] = route
        return ResponseEntity.accepted().body(body)
    }

    fun fail(route: String, game: Game? = null, player: Player? = null): ResponseEntity<Any> {
        if (game != null) {
            influx.game(game, route)
        }
        if (player != null) {
            influx.activity(player, route)
        }
        body["fail"] = route
        return ResponseEntity.accepted().body(body)
    }

    fun player(route: String, player: Player, growth: Boolean = false): ResponseEntity<Any> {
        if (growth) {
            influx.growth(player)
        } else {
            influx.activity(player, route)
        }
        repositoryPlayer.save(player)
        return ResponseEntity.ok().body(player)
    }


}