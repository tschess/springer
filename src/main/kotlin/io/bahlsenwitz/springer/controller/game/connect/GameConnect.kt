package io.bahlsenwitz.springer.controller.game.connect

import io.bahlsenwitz.springer.model.common.SKIN
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class GameConnect(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun connect(requestConnect: RequestConnect): ResponseEntity<Any> {
        val uuid0: UUID = UUID.fromString(requestConnect.id_game)!!
        val game: Game = repositoryGame.findById(uuid0).get()

        val uuid1: UUID = UUID.fromString(requestConnect.id_player)!!
        val player: Player = repositoryPlayer.findById(uuid1).get()

        return ResponseEntity.ok(GameConnectCore(player = player, game = game))
    }

    data class RequestConnect(
        val id_game: String,
        val id_player: String
    )

    class GameConnectCore(player: Player, game: Game) {
        private val info: Info = getInfo(player, game)
        val white: Boolean = info.white
        val skin: SKIN = info.skin
        val state: List<List<String>> = game.state!!
        val turn: CONTESTANT = game.turn
        val status: STATUS = game.status
        val highlight: String = game.highlight
        val on_check: Boolean = game.on_check

        companion object {
            data class Info(
                val white: Boolean,
                val skin: SKIN
            )

            fun getInfo(player: Player, game: Game): Info {
                var white: Boolean = true
                var skin: SKIN = game.white_skin
                if (game.white != player) {
                    white = false
                    skin = game.black_skin
                    return Info(white, skin)
                }
                return Info(white, skin)
            }
        }
    }
}