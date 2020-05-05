package io.bahlsenwitz.springer.controller.game.menu.create

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.ChessConfig

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

class GameRematch(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    data class RequestRematch(
        val id_self: String, //self
        val id_other: String,
        val config: Int, //0, 1, 2, 3
        val white: Boolean //to be white...
    )

    fun rematch(requestRematch: RequestRematch): ResponseEntity<Any> {
        val uuid1: UUID = UUID.fromString(requestRematch.id_self)!!
        val uuid2: UUID = UUID.fromString(requestRematch.id_other)!!
        val playerSelf: Player = repositoryPlayer.findById(uuid1).get()
        val playerOther: Player = repositoryPlayer.findById(uuid2).get()

        var config: List<List<String>> = ChessConfig().getConfigChess()
        if (requestRematch.config == 0) {
            config = playerSelf.config0
        }
        if (requestRematch.config == 1) {
            config = playerSelf.config1
        }
        if (requestRematch.config == 2) {
            config = playerSelf.config2
        }
        val white: Boolean = requestRematch.white
        val game: Game = Game(white = playerSelf, black = playerOther, challenger = CONTESTANT.WHITE, state = config)
        if(!white){
            game.white = playerOther
            game.black = playerSelf
            game.challenger = CONTESTANT.BLACK
        }
        Influx().game(game_id = game.id.toString(), route = "rematch")
        return ResponseEntity.ok(repositoryGame.save(game))
    }
}