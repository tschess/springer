package io.bahlsenwitz.springer.controller.game.menu

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Constant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.ZonedDateTime
import java.util.*

class GameRecent(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {
    fun recent(id_player: String): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(id_player)!!
        val player: Player = repositoryPlayer.findById(uuid).get()
        khttp.post(url = "${Constant().INFLUX}write?db=tschess", data = "menu id=\"${player.id}\",route=\"recent\"")

        val playerList: List<Game> = repositoryGame.findPlayerList(uuid)
        val playerListFilter: List<Game> =
            playerList.filter {
                it.status == STATUS.RESOLVED && it.condition != CONDITION.REFUSED && it.condition != CONDITION.RESCIND
            }
        if (playerListFilter.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"recent\": \"TBD\"}")
        }
        val recent: Game = playerListFilter.sortedWith(RecentCmp)[0]
        return ResponseEntity.ok(recent)
    }

    class RecentCmp {
        companion object : Comparator<Game> {
            override fun compare(a: Game, b: Game): Int {
                val updateA: ZonedDateTime = Constant().getDate(a.updated)
                val updateB: ZonedDateTime = Constant().getDate(b.updated)
                val updateAB: Boolean = updateA.isBefore(updateB)
                if (updateAB) {
                    return -1
                }
                return 1
            }
        }
    }
}

