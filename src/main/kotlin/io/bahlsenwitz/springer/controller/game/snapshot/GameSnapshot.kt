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

        val game_id: UUID = UUID.fromString(requestSnapshot.game_id)!!
        val game: Game = repositoryGame.findById(game_id).get()

        val self_id: UUID = UUID.fromString(requestSnapshot.self_id)!!
        val player: Player = repositoryPlayer.findById(self_id).get()

        return ResponseEntity.ok(GameSnapshotEndgame(player, game))
    }

    data class RequestSnapshot(
        val game_id: String,
        val self_id: String
    )

    class GameSnapshotEndgame(player: Player, game: Game) {
        private val info: Info = getInfo(player, game)

        val state: List<List<String>> = game.state!!
        val outcome: OUTCOME = game.outcome
        val moves: Int = game.moves
        val username_white: String = game.white.username
        val username_black: String = game.black.username

        val canonical: Boolean = info.canonical
        val winner_skin: SKIN = info.skin_winner

        companion object {

            data class Info(
                val canonical: Boolean,
                val skin_winner: SKIN
            )

            fun getInfo(player: Player, game: Game): Info {
                var canonical: Boolean = true
                var skin_winner: SKIN = SKIN.DEFAULT

                if(game.white == player){ //playing white
                    if (game.winner == CONTESTANT.WHITE) { //white wins
                        skin_winner = game.white_skin
                        return Info(canonical, skin_winner)
                    } //white lost, or draw...
                    if (game.winner == CONTESTANT.BLACK) { //black won
                        skin_winner = game.black_skin
                        return Info(canonical, skin_winner)
                    }
                    return Info(canonical, skin_winner)
                } //playing black
                canonical = false
                if (game.winner == CONTESTANT.BLACK) { //black won
                    skin_winner = game.black_skin
                    return Info(canonical, skin_winner)
                }
                if (game.winner == CONTESTANT.WHITE) { //white wins
                    skin_winner = game.white_skin
                    return Info(canonical, skin_winner)
                }
                return Info(canonical, skin_winner)
            }
        }
    }
}

