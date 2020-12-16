package io.bahlsenwitz.springer.controller.player.request

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.push.Pusher
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Config
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

class PlayerStart(private val repositoryPlayer: RepositoryPlayer, val repositoryGame: RepositoryGame) {

    data class RequestStart(
        val username: String,
        val password: String,
        val device: String
    )

    private val config: Config
    private val rating: Rating
    private val encoder: BCryptPasswordEncoder

    init {
        this.config = Config()
        this.rating = Rating(repositoryPlayer = repositoryPlayer)
        this.encoder = BCryptPasswordEncoder()
    }

    fun login(requestLogin: RequestStart): ResponseEntity<Any> {
        val conflict: Player? = repositoryPlayer.findByDevice(requestLogin.device)
        if (conflict != null) {
            conflict.device = null
            repositoryPlayer.save(conflict)
        }
        val player: Player? = repositoryPlayer.findByUsername(requestLogin.username)
        if (player == null) {
            val body: MutableMap<String, String> = HashMap()
            body["unknown"] = "login"
            return ResponseEntity.ok().body(body)
        }
        val match: Boolean = encoder.matches(requestLogin.password, player.password)
        if (match) {
            player.device = requestLogin.device
            player.updated = DateTime().getDate()
            return ResponseEntity.ok().body(repositoryPlayer.save(player))
        }
        val body: MutableMap<String, String> = HashMap()
        body["invalid"] = "login"
        return ResponseEntity.ok().body(body)
    }

    fun create(requestCreate: RequestStart): ResponseEntity<Any> {
        val conflict: Boolean = repositoryPlayer.findByUsername(requestCreate.username) != null
        if (conflict) {
            val body: MutableMap<String, String> = HashMap()
            body["reserved"] = "create"
            return ResponseEntity.ok().body(body)
        }
        var player = Player(
            username = requestCreate.username,
            password = encoder.encode(requestCreate.password),
            device = requestCreate.device
        )
        player = rating.addition(player)
        this.seedInviteInit(player)

        if (requestCreate.device.length > 16) {
            //return output.player(player = player, route = "create", growth = "ios")
        } else {
            //return output.player(player = player, route = "create", growth = "android")
        }
        player.device = requestCreate.device
        player.updated = DateTime().getDate()
        return ResponseEntity.ok().body(repositoryPlayer.save(player))
    }

    private fun seedInviteInit(player: Player) {
        val opponent: Player = repositoryPlayer.findByUsername("sme")!!
        val index: Int = (0..3).random()
        val state: List<List<String>> = config.get(index, opponent)
        val game = Game(state = state, white = player, black = opponent)
        repositoryGame.save(game)
        Pusher().notify(opponent)
    }
}

