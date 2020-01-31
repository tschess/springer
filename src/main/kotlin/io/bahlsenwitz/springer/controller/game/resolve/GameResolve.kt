package io.bahlsenwitz.springer.controller.game.resolve

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class GameResolve(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun resolve(requestResolve: RequestResolve): ResponseEntity<Any> {

        val uuid0: UUID = UUID.fromString(requestResolve.game)!!
        val game: Game = repositoryGame.findById(uuid0).get()

        val uuid1: UUID = UUID.fromString(requestResolve.player)!!
        val player: Player = repositoryPlayer.findById(uuid1).get()


        setNotification(game = game, player = player, repositoryPlayer = repositoryPlayer)
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    data class RequestResolve(
        val game: String,
        val player: String,
        val turn: String,
        val outcome: String
    )

    companion object {

        private val DATE_TIME_GENERATOR = GeneratorDateTime()

        fun setNotification(game: Game, player: Player, repositoryPlayer: RepositoryPlayer) {
            if (game.white == player) {
                game.black.notify = true
                repositoryPlayer.save(game.black)
                return
            }
            game.white.notify = true
            repositoryPlayer.save(game.white)
        }
    }
}