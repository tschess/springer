package io.bahlsenwitz.springer.controller.game.menu.create

import io.bahlsenwitz.springer.model.rating.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.ConfigState
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.push.Pusher
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameQuick(repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {
    private val pusher: Pusher = Pusher()
    private val dateTime: DateTime = DateTime()
    private val configState: ConfigState = ConfigState()
    private val output: Output = Output(repositoryGame = repositoryGame)
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun quick(requestQuick: RequestCreate): ResponseEntity<Any> {
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(requestQuick.id_self)!!).get()
        playerSelf.date = dateTime.getDate()
        rating.update(playerSelf, RESULT.WIN)
        val playerOther: Player = repositoryPlayer.findById(UUID.fromString(requestQuick.id_other)!!).get()
        playerOther.note_value = true
        /* * */
        pusher.notify(playerOther)
        /* * */
        repositoryPlayer.save(playerOther)
        val state: List<List<String>> = configState.generateState(configState.get(requestQuick.config, playerSelf))
        val game = Game(
            state = state,
            white = playerSelf,
            black = playerOther,
            challenger = CONTESTANT.WHITE,
            status = STATUS.ONGOING
        )
        return output.game(game = game)
    }


}