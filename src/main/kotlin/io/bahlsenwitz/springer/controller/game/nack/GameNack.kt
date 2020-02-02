package io.bahlsenwitz.springer.controller.game.nack

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

//curl --header "Content-Type: application/json" --request POST --data '{"game":"11111111-1111-1111-1111-111111111111", "player": "99999999-9999-9999-9999-999999999999"}' http://localhost:8080/game/snapshot
class GameNack(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun nack(updateNack: UpdateNack): ResponseEntity<Any> {

        val uuid0: UUID = UUID.fromString(updateNack.id_game)!!
        val game: Game = repositoryGame.findById(uuid0).get()
        game.status = STATUS.DECLINED
        repositoryGame.save(game)


        val uuid1: UUID = UUID.fromString(updateNack.id_player)!!
        val player: Player = repositoryPlayer.findById(uuid1).get()
        //player.elo -= ...
        //TODO: ^^^reduce elo
        //TODO: re-sort leaderboard...

        //TODO: think about that wrt rescind...

        //TODO: maybe send back the new leaderboard...
        return ResponseEntity.status(HttpStatus.OK).body("{\"nack\": \"${game.id}\"}")
    }

    data class UpdateNack(
        val id_game: String,
        val id_player: String
    )
}