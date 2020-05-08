package io.bahlsenwitz.springer.controller.player.start

import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.ConfigState
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Output
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class PlayerCreate(private val repositoryPlayer: RepositoryPlayer, private val repositoryGame: RepositoryGame) {

    private val dateTime: DateTime = DateTime()
    private val configState: ConfigState = ConfigState()
    private val output: Output = Output(repositoryPlayer)
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
        seedGameInit(player)
        player = rating.addition(player)
        return output.player("device", player, true)
    }

    //TODO: draw from quick...
    private fun seedGameInit(player: Player) {
        val opponent: Player = repositoryPlayer.findByUsername("sme")!!
        opponent.updated = dateTime.getDate()
        repositoryPlayer.save(opponent)
        val config: Int = (0..3).random()
        val state: List<List<String>> = configState.generateState(configState.get(config, player))
        val game = Game(
            state = state,
            white = player,
            black = opponent,
            challenger = CONTESTANT.WHITE,
            status = STATUS.ONGOING
        )
        repositoryGame.save(game)
    }

}