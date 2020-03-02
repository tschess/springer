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
            gameListInbound.sortedWith(GameComparator)
            val list: MutableList<Game> = mutableListOf()
            for (gae: GameInbound in gameListInbound) {
                list.add(gae.game)
            }
            return ResponseEntity.ok(list.sortedWith(Game))
        }
        gameList = playerListFilter.subList(indexFrom, indexTo + 1)
        for (game: Game in gameList) {
            val gameInbound = GameInbound(player = player, game = game)
            gameListInbound.add(gameInbound)
        }
        gameListInbound.sortedWith(GameComparator)
        val list: MutableList<Game> = mutableListOf()
        for (gae: GameInbound in gameListInbound) {
            list.add(gae.game)
        }
        return ResponseEntity.ok(list.sortedWith(Game))
    }

    data class RequestActual(
        val id: String,
        val index: Int,
        val size: Int
    )

    class GameComparator {

        companion object : Comparator<GameInbound> {

            override fun compare(a: GameInbound, b: GameInbound): Int {
                val inboundA: Boolean = a.stats.inbound
                val inboundB: Boolean = b.stats.inbound
                val invitationA: Boolean = a.stats.invitation
                val invitationB: Boolean = b.stats.invitation
                val updateA: ZonedDateTime = Constant().getDate(a.updated)
                val updateB: ZonedDateTime = Constant().getDate(b.updated)
                val updateAB: Boolean = updateA.isBefore(updateB)
                if (inboundA) { //a in
                    if (inboundB) { //b in
                        if (!invitationA) { //a game
                            if (!invitationB) { //b game
                                if (updateAB) {
                                    return 1 //a < b
                                }
                                return -1 //b < a
                            } //a is an inbound game, b is an inbound invitation
                            return 1 //a < b
                        } //a is an inbound invitation
                        if (!invitationB) { //b is an inbound game
                            return -1 //b < a
                        }
                    } // b is outbound, a is inbound
                    return -1 //a < b
                } //a is outbound
                if (!inboundB) { //b is outbound
                    if (!invitationA) { //a is game
                        if (!invitationB) { //b is game
                            if (updateAB) {
                                return -1 //a < b
                            }
                            return 1 //b < a
                        } //b is invite
                        return -1 //a < b
                    } //a is invite
                    if (!invitationB) { //b is game
                        return 1 //b < a
                    } // both outbound invite
                    if (updateAB) {
                        return -1 //a < b
                    }
                    return 1 //b < a
                }
                return 0
            }
        }
    }
}





