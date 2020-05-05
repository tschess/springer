package io.bahlsenwitz.springer.controller.game.menu.invite

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.accepted
import java.util.*

class GameNack(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val influx: Influx = Influx()
    private val dateTime: DateTime = DateTime()
    private val rating: Rating = Rating(repositoryPlayer)

    data class UpdateNack(val id_game: String, val id_self: String)

    fun nack(updateNack: UpdateNack): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(updateNack.id_game)!!).get()
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.REFUSED
        repositoryGame.save(game)

        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(updateNack.id_self)!!).get()
        playerSelf.date = dateTime.getDate()
        rating.update(playerSelf, RESULT.LOSS)

        influx.game(game, "nack")
        return ResponseEntity.ok(accepted())
    }
}