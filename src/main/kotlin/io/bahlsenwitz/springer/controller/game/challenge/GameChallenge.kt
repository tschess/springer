package io.bahlsenwitz.springer.controller.game.challenge

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
class GameChallenge(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun challenge(requestChallenge: RequestChallenge): ResponseEntity<Any> {

        val uuid0: UUID = UUID.fromString(requestChallenge.white)!! //opp
        val white: Player = repositoryPlayer.findById(uuid0).get()


        //TODO: VALIDATION CRITERIA...


        val uuid1: UUID = UUID.fromString(requestChallenge.black)!! //self
        val black: Player = repositoryPlayer.findById(uuid1).get()
        val black_skin: SKIN = SKIN.valueOf(requestChallenge.skin)

        var config: List<List<String>> = traditionalConfig()
        if (requestChallenge.config == 0) {
            config = black.config0
        }
        if (requestChallenge.config == 1) {
            config = black.config1
        }
        if (requestChallenge.config == 2) {
            config = black.config2
        }

        val game = Game(
            white = white,
            black = black,
            black_skin = black_skin,
            challenger = CONTESTANT.BLACK,
            state = config,
            date_create = DATE_TIME_GENERATOR.rightNowString()
        )
        repositoryGame.save(game)

        white.notify = true
        repositoryPlayer.save(white)

        return ResponseEntity.status(HttpStatus.OK).body("{\"challenge\": \"${game.id}\"}")
    }

    data class RequestChallenge(
        val white: String,
        val black: String, //self
        val skin: String, //skin_black
        val config: Int //0, 1, 2, 3
    )

    companion object {

        private val DATE_TIME_GENERATOR = GeneratorDateTime()

        fun traditionalConfig(): List<List<String>> {
            val r0 = arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
            val r1 = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
            return arrayListOf(r1, r0)
        }
    }
}