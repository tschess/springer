package io.bahlsenwitz.springer.controller.player.request

import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Config
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class PlayerStart(private val repositoryPlayer: RepositoryPlayer, val repositoryGame: RepositoryGame) {

    data class RequestStart(
        val username: String,
        val password: String,
        val device: String
    )

    private val config: Config = Config()
    private val output: Output = Output(repositoryPlayer)
    private val rating: Rating = Rating(repositoryPlayer = repositoryPlayer)

    fun login(requestLogin: RequestStart): ResponseEntity<Any> {
        val player00: Player? = repositoryPlayer.findByDevice(requestLogin.device)
        if (player00 != null) {
            player00.device = null
            repositoryPlayer.save(player00)
        }

        //TODO: unknown
        val player: Player =
            repositoryPlayer.findByUsername(requestLogin.username) ?: return output.terminal(
                result = "unknown",
                route = "login"
            )

        if (BCryptPasswordEncoder().matches(requestLogin.password, player.password)) {
            player.device = requestLogin.device
            return output.player(player = player, route = "login")
        }

        //TODO: password
        return output.terminal(result = "invalid", route = "login")
    }

    fun create(requestCreate: RequestStart): ResponseEntity<Any> {
        val conflict: Boolean = repositoryPlayer.findByUsername(requestCreate.username) != null
        if (conflict) {

            //TODO: reserved
            return output.terminal(result = "reserved", route = "create")
        }

        var player = Player(
            username = requestCreate.username,
            password = BCryptPasswordEncoder().encode(requestCreate.password),
            device = requestCreate.device
        )
        player = rating.addition(player)

        seedGameInit(player)

        if(requestCreate.device.length > 16){
            return output.player(player = player, route = "create", growth = "ios")
        }
        return output.player(player = player, route = "create", growth = "android")
    }

    /**
     *
     * CHANGE THIS TO AN INVITE!!!!
     *
     * not a game...
     *
     */
    private fun seedGameInit(player: Player) {
        val opponent: Player = repositoryPlayer.findByUsername("sme")!!

        repositoryPlayer.save(opponent)
        val index: Int = (0..3).random()
        val state: List<List<String>> = config.get(index, opponent)
        val game = Game(
            state = state,
            white = player,
            black = opponent,
            challenger = CONTESTANT.BLACK)
        repositoryGame.save(game)
    }
}

