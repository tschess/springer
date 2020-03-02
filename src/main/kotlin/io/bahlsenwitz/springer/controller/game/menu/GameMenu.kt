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

class GameMenu(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun actual(requestActual: RequestActual): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(requestActual.id)!!
        val player: Player = repositoryPlayer.findById(uuid).get()

        khttp.post(url = "${Constant().INFLUX}write?db=tschess", data = "menu id=\"${player.id}\",route=\"actual\"")

        player.note = false
        repositoryPlayer.save(player)

        val playerList: List<Game> = repositoryGame.findPlayerList(uuid)
        val playerListFilter: List<Game> =
            playerList.filter {
                it.status == STATUS.PROPOSED ||
                        it.status == STATUS.ONGOING ||
                        (it.status == STATUS.RESOLVED && it.condition != CONDITION.REFUSED && it.condition != CONDITION.RESCIND)
            }
        val gameListInbound: MutableList<GameInbound> = mutableListOf()
        val gameList: List<Game>

        val pageIndex: Int = requestActual.index
        val pageSize: Int = requestActual.size

        val indexFrom: Int = pageIndex * pageSize
        val indexTo: Int = indexFrom + pageSize

        if (playerListFilter.lastIndex < indexFrom) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"actual\": \"EOL\"}")
        }
        if (playerListFilter.lastIndex + 1 <= indexTo) {
            gameList = playerListFilter.subList(indexFrom, playerListFilter.lastIndex + 1)
            for (game: Game in gameList) {
                val gameInbound = GameInbound(player = player, game = game)
                gameListInbound.add(gameInbound)
            }
            gameListInbound.sortedWith(InboundComparator)
            val list: MutableList<Game> = mutableListOf()
            for (gae: GameInbound in gameListInbound) {
                list.add(gae.game)
            }
            return ResponseEntity.ok(list)
        }
        gameList = playerListFilter.subList(indexFrom, indexTo + 1)
        for (game: Game in gameList) {
            val gameInbound = GameInbound(player = player, game = game)
            gameListInbound.add(gameInbound)
        }
        gameListInbound.sortedWith(InboundComparator)
        val list: MutableList<Game> = mutableListOf()
        for (gae: GameInbound in gameListInbound) {
            list.add(gae.game)
        }
        return ResponseEntity.ok(list)
    }

    data class RequestActual(
        val id: String,
        val index: Int,
        val size: Int
    )

    class InboundComparator {
        companion object : Comparator<GameInbound> {
            override fun compare(a: GameInbound, b: GameInbound): Int {
                val inbA: Boolean = a.stats.inbound
                val inbB: Boolean = b.stats.inbound
                if (inbA) {
                    if (inbB) {
                        return PropComparator.compare(a.game, b.game)
                    }
                    return -1
                }
                if (inbB) {
                    return 1
                }
                return PropComparator.compare(b.game, a.game)
            }
        }
    }

    class PropComparator {
        companion object : Comparator<Game> {
            override fun compare(a: Game, b: Game): Int {
                val propA: Boolean = a.status == STATUS.PROPOSED
                val propB: Boolean = b.status == STATUS.PROPOSED
                if (propA) {
                    if (propB) {
                        return Game.compare(a, b)
                    }
                    return -1
                }
                if (propB) {
                    return 1
                }
                return Game.compare(a, b)
            }
        }
    }
}





