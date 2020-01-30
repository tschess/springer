package io.bahlsenwitz.springer.controller.game.snapshot

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.OUTCOME
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*


//curl --header "Content-Type: application/json" --request POST --data '{"id":"efac3243-c71c-42f3-9f12-117cc7de6fa7", "index": 0, "size": 1}' http://localhost:8080/game/snapshot
class GameSnapshot(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun snapshot(requestSnapshot: RequestSnapshot): ResponseEntity<Any> {

        val gameID: UUID = UUID.fromString(requestSnapshot.game)!!
        val game: Game = repositoryGame.findById(gameID).get()

        val playerID: UUID = UUID.fromString(requestSnapshot.player)!!
        val player: Player = repositoryPlayer.findById(playerID).get()

        return ResponseEntity.ok(GameSnapshotEndgame(player, game))
    }

    data class RequestSnapshot(
        val game: String,
        val player: String
    )
}

class GameSnapshotEndgame(player: Player, game: Game) {

    val state: List<List<String>> = game.state!!
    val moves: Int = game.moves
    val outcome: OUTCOME = game.outcome
    val white: Boolean = getWhite(player, game)

    companion object {

        fun getWhite(player: Player, game: Game): Boolean {
            if (game.white == player) {
                return true
            }
            return false
        }
    }
}