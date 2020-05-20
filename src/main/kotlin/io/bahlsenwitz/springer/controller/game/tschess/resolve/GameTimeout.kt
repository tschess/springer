package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.ConfigState
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameTimeout(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val dateTime: DateTime = DateTime()
    private val configState: ConfigState = ConfigState()
    private val output: Output = Output(repositoryGame = repositoryGame)
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun timeout(updateResign: GameResign.UpdateResign): ResponseEntity<Any>? {
        val game: Game = repositoryGame.findById(UUID.fromString(updateResign.id_game)!!).get()
        if (game.status == STATUS.RESOLVED) {
            return output.terminal(result = "fail", route = "timeout")
        }
        val date: String = dateTime.getDate()
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(updateResign.id_self)!!).get()
        playerSelf.updated = date
        playerSelf.note = false
        repositoryPlayer.save(playerSelf)
        game.highlight = "TBD"
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.TIMEOUT
        game.state = configState.poisonReveal(game.state!!)
        if(game.white == playerSelf){
            game.turn = CONTESTANT.WHITE
        } else {
            game.turn = CONTESTANT.BLACK
        }
        rating.resolve(game)
        return output.update(route = "timeout", game = game)
    }
}