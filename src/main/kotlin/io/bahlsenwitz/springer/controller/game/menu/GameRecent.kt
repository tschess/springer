package io.bahlsenwitz.springer.controller.game.menu

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.push.Influx
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.time.ZonedDateTime
import java.util.*

class GameRecent(private val repositoryGame: RepositoryGame,
                 private val repositoryPlayer: RepositoryPlayer) {

    private val influx: Influx = Influx()
    private val output: Output = Output()

    fun recent(id_player: String): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(id_player)!!

        val playerList: List<Game> = repositoryGame.findPlayerList(uuid)
        val playerListFilter: List<Game> =
            playerList.filter {
                it.status == STATUS.RESOLVED &&
                        it.condition != CONDITION.REFUSED &&
                        it.condition != CONDITION.RESCIND &&
                        it.condition != CONDITION.EXPIRED
            }

        if (playerListFilter.isEmpty()) {
            return output.terminal(result = "fail", route = "recent")
        }
        val recent: Game = playerListFilter.sortedWith(RecentCmp).last()

        /* * */
        val player: Player = repositoryPlayer.findById(uuid).get()
        influx.activity(player, "recent")
        /* * */
        return ResponseEntity.ok(recent)
    }

    class RecentCmp {
        companion object : Comparator<Game> {
            override fun compare(a: Game, b: Game): Int {
                val updateA: ZonedDateTime = DateTime().getDate(a.updated)
                val updateB: ZonedDateTime = DateTime().getDate(b.updated)
                val updateAB: Boolean = updateA.isBefore(updateB)
                if (updateAB) {
                    return -1
                }
                return 1
            }
        }
    }
}

