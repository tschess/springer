package io.bahlsenwitz.springer.controller.game.menu.create

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

//curl --header "Content-Type: application/json" --request POST --data '{"game":"11111111-1111-1111-1111-111111111111", "player": "99999999-9999-9999-9999-999999999999"}' http://localhost:8080/game/snapshot
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

        var config: List<List<String>> = traditionalConfig()
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
        val state: List<List<String>> = generateState(config,white)

        val game: Game = Game(white = playerSelf, black = playerOther, challenger = CONTESTANT.WHITE, state = state)
        if(!white){
            game.white = playerOther
            game.black = playerSelf
            game.challenger = CONTESTANT.BLACK
        }

        Influx().game(game_id = game.id.toString(), route = "rematch")
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    companion object {

        fun traditionalConfig(): List<List<String>> {
            val row0: List<String> =
                arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
            val row1: List<String> = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
            return arrayListOf(row0, row1)
        }

        fun generateState(config: List<List<String>>, white: Boolean): List<List<String>> {
            val empty: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            if(white){
                return arrayListOf(config[0], config[1], empty, empty, empty, empty, empty, empty)
            }
            return arrayListOf(empty, empty, empty, empty, empty, empty, config[0], config[1])
        }
    }
}