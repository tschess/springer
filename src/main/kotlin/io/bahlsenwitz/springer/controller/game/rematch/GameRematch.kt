package io.bahlsenwitz.springer.controller.game.rematch

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.common.SKIN
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

//curl --header "Content-Type: application/json" --request POST --data '{"game":"11111111-1111-1111-1111-111111111111", "player": "99999999-9999-9999-9999-999999999999"}' http://localhost:8080/game/snapshot
class GameRematch(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun rematch(requestRematch: RequestRematch): ResponseEntity<Any> {

        val uuid0: UUID = UUID.fromString(requestRematch.id)!!
        val game: Game = repositoryGame.findById(uuid0).get()


        


        return ResponseEntity.ok(repositoryGame.save(game))
    }

    data class RequestRematch(
        val id: String,
        val challenger: String, //self
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
    }
}