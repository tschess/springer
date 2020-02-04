package io.bahlsenwitz.springer.controller.game.rematch

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.common.SKIN
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

    fun rematch(requestRematch: RequestRematch): ResponseEntity<Any> {

        val uuid1: UUID = UUID.fromString(requestRematch.self)!!
        val uuid2: UUID = UUID.fromString(requestRematch.other)!!
        val self: Player = repositoryPlayer.findById(uuid1).get()
        val other: Player = repositoryPlayer.findById(uuid2).get()
        var white: Player = self
        var black: Player = other
        var challenger: CONTESTANT = CONTESTANT.WHITE
        var white_skin: SKIN = SKIN.valueOf(requestRematch.skin)
        var black_skin: SKIN = SKIN.DEFAULT

        var config: List<List<String>> = traditionalConfig()
        if (requestRematch.config == 0) {
            config = white.config0
        }
        if (requestRematch.config == 1) {
            config = white.config1
        }
        if (requestRematch.config == 2) {
            config = white.config2
        }

        val uuid0: UUID = UUID.fromString(requestRematch.id)!!
        val game0: Game = repositoryGame.findById(uuid0).get()

        var state: List<List<String>> = generateState(config, true)
        if(game0.white == white){
            white = other
            black = self
            challenger = CONTESTANT.BLACK
            white_skin = SKIN.DEFAULT
            black_skin = SKIN.valueOf(requestRematch.skin)
            state = generateState(config, false)
        }

        val game1: Game = Game(
            white = white,
            black = black,
            challenger = challenger,
            white_skin = white_skin,
            black_skin = black_skin,
            state = state,
            created = DATE_TIME_GENERATOR.rightNowString()
        )
        repositoryGame.save(game1)
        return ResponseEntity.status(HttpStatus.OK).body("{\"challenge\": \"${game1.id}\"}")
    }

    data class RequestRematch(
        val id: String,
        val self: String, //self
        val other: String,
        val skin: String, //skin (gotta calc black or white)
        val config: Int //0, 1, 2, 3
    )

    companion object {

        private val DATE_TIME_GENERATOR = GeneratorDateTime()

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