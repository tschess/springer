package io.bahlsenwitz.springer.controller.game.snapshot

import io.bahlsenwitz.springer.model.common.SKIN
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.OUTCOME
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

//curl --header "Content-Type: application/json" --request POST --data '{"game":"11111111-1111-1111-1111-111111111111", "player": "99999999-9999-9999-9999-999999999999"}' http://localhost:8080/game/snapshot
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

    class GameSnapshotEndgame(player: Player, game: Game) {
        private val info: Info = getInfo(player, game)

        val state: List<List<String>> = game.state!!
        val moves: Int = game.moves
        val outcome: OUTCOME = game.outcome
        val white: Boolean = info.white
        val skin: SKIN = info.skin

        companion object {

            data class Info(
                val white: Boolean,
                val skin: SKIN
            )

            fun getInfo(player: Player, game: Game): Info {
                var white: Boolean = true
                var skin: SKIN = game.white_skin
                if (game.winner == CONTESTANT.WHITE) {
                    if (game.white == player) {
                        return Info(white, skin)
                    }
                    white = false
                    return Info(white, skin)
                }
                if (game.winner == CONTESTANT.BLACK) {
                    skin = game.black_skin
                    if (game.black == player) {
                        white = false
                        return Info(white, skin)
                    }
                    return Info(white, skin)
                } //DRAW
                skin = SKIN.DEFAULT
                if (game.black == player) {
                    white = false
                    return Info(white, skin)
                }
                return Info(white, skin)
            }
        }
    }
}

