package io.bahlsenwitz.springer.controller

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.util.*

class Output(private val repositoryPlayer: RepositoryPlayer? = null, private val repositoryGame: RepositoryGame? = null) {

    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()
    private var body: MutableMap<String, String> = HashMap()

    fun terminal(result: String, route: String, game: Game? = null, player: Player? = null): ResponseEntity<Any> {
        influx(route, game, player)
        body[result] = route
        return ResponseEntity.ok().body(body)
    }

    fun update(route: String, game: Game): ResponseEntity<Any> {
        influx(route, game, null)
        game.updated = dateTime.getDate()
        repositoryGame!!.save(game)
        body["success"] = route
        return ResponseEntity.ok().body(body)
    }

    fun game(route: String, game: Game): ResponseEntity<Any> {
        influx(route = route, game = game)
        game.updated = dateTime.getDate()
        repositoryGame!!.save(game)
        return ResponseEntity.ok().body(game)
    }

    fun player(route: String, player: Player, growth: Boolean = false): ResponseEntity<Any> {
        if (growth) {
            influx.growth(player)
        } else {
            influx.activity(player, route)
        }
        player.updated = dateTime.getDate()
        repositoryPlayer!!.save(player)
        return ResponseEntity.ok().body(player)
    }

    fun quick(player00: Player, player01: Player): ResponseEntity<Any> {
        influx.activity(player00, "quick")
        player00.updated = dateTime.getDate()
        player01.note = true
        repositoryPlayer!!.saveAll(listOf(player00, player01))
        return ResponseEntity.ok().body(player01)
    }

    private fun influx(route: String, game: Game? = null, player: Player? = null) {
        if (game != null) {
            influx.game(game, route)
            return
        }
        influx.activity(player, route)
    }

}