package io.bahlsenwitz.springer.controller.player.start

import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.ConfigState
import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class PlayerCreate(private val repositoryPlayer: RepositoryPlayer, private val repositoryGame: RepositoryGame) {

    private val configState: ConfigState = ConfigState()
    private val output: Output =
        Output(repositoryPlayer)
    private val rating: Rating = Rating(repositoryPlayer = repositoryPlayer)

    fun create(requestCreate: RequestStart): ResponseEntity<Any> {
        val conflict: Boolean = repositoryPlayer.findByUsername(requestCreate.username) != null
        if (conflict) {
            return output.terminal(result = "fail", route = "create")
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

    //TODO: draw from quick...
    private fun seedGameInit(player: Player) {
        val opponent: Player = repositoryPlayer.findByUsername("sme")!!
        opponent.note_value = true
        repositoryPlayer.save(opponent)
        val config: Int = (0..3).random()
        val state: List<List<String>> = configState.generateState(configState.get(config, player))
        val game = Game(
            state = state,
            white = player,
            black = opponent,
            challenger = CONTESTANT.WHITE,
            status = STATUS.ONGOING,
            condition = CONDITION.PUSH
        )
        repositoryGame.save(game)
    }

}