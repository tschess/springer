package io.bahlsenwitz.springer.controller.game.request

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.model.rating.RESULT
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import java.util.*

class GameNack(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    data class UpdateNack(
        val id_game: String,
        val id_self: String
    )

    private val dateTime: DateTime = DateTime()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun nack(updateNack: UpdateNack): ResponseEntity<Any> {
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(updateNack.id_self)!!).get()
        playerSelf.date = dateTime.getDate()
        rating.update(playerSelf, RESULT.LOSS)
        val game: Game = repositoryGame.findById(UUID.fromString(updateNack.id_game)!!).get()
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.REFUSED
        game.updated = dateTime.getDate()
        repositoryGame.save(game)
        //return output.player(player = playerSelf, route = "nack") //to update your header...  //ack
        playerSelf.updated = dateTime.getDate()
        return ResponseEntity.ok().body(repositoryPlayer.save(playerSelf))
    }
}

