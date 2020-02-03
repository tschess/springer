package io.bahlsenwitz.springer.controller.game.ack

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.common.SKIN
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

//curl --header "Content-Type: application/json" --request POST --data '{"id_game":"99999999-9999-9999-9999-999999999999", "id_player": "99999999-9999-9999-9999-999999999999"}' http://localhost:8080/game/ack
class GameAck(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun ack(requestAck: RequestAck): ResponseEntity<Any> {

        val uuid0: UUID = UUID.fromString(requestAck.id_game)!!
        val game: Game = repositoryGame.findById(uuid0).get()

        val uuid1: UUID = UUID.fromString(requestAck.id_player)!!
        val player: Player = repositoryPlayer.findById(uuid1).get()

        var config: List<List<String>> = traditionalConfig()
        if (requestAck.config == 0) {
            config = player.config0
        }
        if (requestAck.config == 1) {
            config = player.config1
        }
        if (requestAck.config == 2) {
            config = player.config2
        }
        val state: List<List<String>> = generateState(config, game.state!!)

        game.state = state
        game.status = STATUS.ONGOING
        game.date_update = DATE_TIME_GENERATOR.rightNowString()

        var white: Boolean = false
        if (game.white == player) {
            white = true
        }
        if (white) {
            game.white_skin = SKIN.valueOf(requestAck.skin)
        } else {
            game.black_skin = SKIN.valueOf(requestAck.skin)
        }
        setNotification(game = game, player = player, repositoryPlayer = repositoryPlayer)
        repositoryGame.save(game)
        return ResponseEntity.ok(GameAck(player = player, game = game))
    }

    data class RequestAck(
        val id_game: String,
        val id_player: String,
        val skin: String,
        val config: Int //0, 1, 2, 3
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

        fun traditionalConfig(): List<List<String>> {
            val r0 = arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
            val r1 = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
            return arrayListOf(r0, r1)
        }

        fun generateState(config: List<List<String>>, state: List<List<String>>): List<List<String>> {
            return arrayListOf(
                config[0],
                config[1],
                state[2],
                state[3],
                state[4],
                state[5],
                state[6],
                state[7]
            )
        }
    }

    class GameAck(player: Player, game: Game) {
        private val info: Info = getInfo(player, game)
        val white: Boolean = info.white
        val skin: SKIN = info.skin
        val state: List<List<String>> = game.state!!

        companion object {
            data class Info(
                val white: Boolean,
                val skin: SKIN
            )

            fun getInfo(player: Player, game: Game): Info {
                var white: Boolean = true
                var skin: SKIN = game.white_skin
                if (game.white == player) {
                    return Info(white, skin)
                }
                white = false
                skin = game.black_skin
                return Info(white, skin)
            }
        }
    }
}