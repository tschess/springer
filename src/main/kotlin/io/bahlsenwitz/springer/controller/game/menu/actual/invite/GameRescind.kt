package io.bahlsenwitz.springer.controller.game.menu.actual.invite

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Constant
import org.springframework.http.ResponseEntity
import java.util.*

class GameRescind(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun rescind(updateRescind: UpdateRescind): ResponseEntity<Any> {
        val uuid0: UUID = UUID.fromString(updateRescind.id_game)!!
        val game: Game = repositoryGame.findById(uuid0).get()
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.RESCIND
        repositoryGame.save(game)

        khttp.post(url = "${Constant().INFLUX}write?db=tschess", data = "game id=\"${game.id}\",route=\"rescind\"")

        val uuid1: UUID = UUID.fromString(updateRescind.id_player)!!
        val player0: Player = repositoryPlayer.findById(uuid1).get()

        val elo: Int = player0.elo - 1
        player0.elo = elo
        repositoryPlayer.save(player0)

        /**
         * LEADERBOARD RECALC
         */
        val playerFindAllList: List<Player> = repositoryPlayer.findAll().sorted()
        playerFindAllList.forEachIndexed forEach@{ index, player ->
            if (player.rank == index + 1) {
                player.disp = 0
                repositoryPlayer.save(player)
                return@forEach
            }
            val disp: Int = player.rank - (index + 1)
            player.disp = disp
            player.date = Constant().getDate()
            val rank: Int = (index + 1)
            player.rank = rank
            repositoryPlayer.save(player)
        }
        //^^^

        val playerX: Player = repositoryPlayer.findById(uuid1).get()
        return ResponseEntity.ok(playerX) //in fact ~ this only needs to return the header info...
    }

    data class UpdateRescind(
        val id_game: String,
        val id_player: String
    )
}