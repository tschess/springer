package io.bahlsenwitz.springer.controller.game.menu.invite

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.common.Elo
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Leaderboard
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.accepted
import java.util.*

class GameNack(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val influx: Influx = Influx()
    private val leaderboard: Leaderboard = Leaderboard(repositoryPlayer)

    data class UpdateNack(val id_game: String, val id_self: String)

    fun nack(updateNack: UpdateNack): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(updateNack.id_game)!!).get()
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.REFUSED
        repositoryGame.save(game)
        
        val playerSelf: Player = repositoryPlayer.findById(UUID.fromString(updateNack.id_self)!!).get()
        val elo00: Int = playerSelf.elo
        val elo01: Elo = Elo(elo00)
        val elo02: Int = elo01.update(RESULT.LOSS, elo00)
        playerSelf.elo = elo02
        repositoryPlayer.save(playerSelf)
        leaderboard.recalc()
        
        influx.game(game, "nack")
        return ResponseEntity.ok(accepted())
    }
}