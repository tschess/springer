package io.bahlsenwitz.springer.controller.game.request

import io.bahlsenwitz.springer.model.rating.RESULT
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameRescind(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val dateTime: DateTime = DateTime()
    private val output: Output =
        Output(repositoryGame = repositoryGame)
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun rescind(updateRescind: UpdateNack): ResponseEntity<Any> {
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(updateRescind.id_self)!!).get()
        playerSelf.date = dateTime.getDate()
        rating.update(playerSelf, RESULT.LOSS)
        val game: Game = repositoryGame.findById(UUID.fromString(updateRescind.id_game)!!).get()
        if(game.challenger != CONTESTANT.WHITE){
            game.white.note_value = false
        } else {
            game.black.note_value = false
        }
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.RESCIND
        game.updated = dateTime.getDate()
        repositoryGame.save(game)
        return output.player(player = playerSelf, route = "rescind") //to update your header...
    }
}