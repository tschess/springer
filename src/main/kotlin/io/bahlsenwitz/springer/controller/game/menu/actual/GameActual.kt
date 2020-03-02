package io.bahlsenwitz.springer.controller.game.menu.actual

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Constant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class GameActual(
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

        val gameCoreActualList: MutableList<GameActualEval> = mutableListOf()
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
                val gameCoreActual =
                    GameActualEval(player = player, game = game)
                gameCoreActualList.add(gameCoreActual)
            }
            val gameCoreActualSort: List<GameActualEval> = gameCoreActualList.sortedWith(
                GameActualEvalComparator
            )
            val list: MutableList<Game> = mutableListOf()
            for (gae: GameActualEval in gameCoreActualSort) {
                list.add(gae.game)
            }
            return ResponseEntity.ok(list)
        }
        gameList = playerListFilter.subList(indexFrom, indexTo + 1)
        for (game: Game in gameList) {
            val gameCoreActual =
                GameActualEval(player = player, game = game)
            gameCoreActualList.add(gameCoreActual)
        }
        val gameCoreActualSort: List<GameActualEval> = gameCoreActualList.sortedWith(
            GameActualEvalComparator
        )
        val list: MutableList<Game> = mutableListOf()
        for (gae: GameActualEval in gameCoreActualSort) {
            list.add(gae.game)
        }
        return ResponseEntity.ok(list)
    }

    data class RequestActual(
        val id: String,
        val index: Int,
        val size: Int
    )

    class GameActualEvalComparator {

        companion object : Comparator<GameActualEval> {

            override fun compare(a: GameActualEval, b: GameActualEval): Int {
                val updateA: ZonedDateTime = Constant().getDate(a.stats.date)
                val updateB: ZonedDateTime = Constant().getDate(b.stats.date)
                val updateAB: Boolean = updateA.isBefore(updateB)

                val historicA: Boolean = a.stats.historic
                val historicB: Boolean = b.stats.historic
                if (historicA) { //histo a
                    if (historicB) { //histo b
                        if (updateAB) {
                            return 1 //a < b
                        }
                        return -1 //b < a
                    } //a is hiistoric, b is not
                    return -1 //b < a
                }

                val inboundA: Boolean = a.stats.inbound
                val inboundB: Boolean = b.stats.inbound
                val invitationA: Boolean = a.stats.invitation
                val invitationB: Boolean = b.stats.invitation



                if (inboundA) { //a in
                    if (inboundB) { //b in
                        if (!invitationA) { //a game
                            if (!invitationB) { //b game
                                if (updateAB) {
                                    return -1 //a < b
                                }
                                return 1 //b < a
                            } //a is an inbound game, b is an inbound invitation
                            return -1 //a < b
                        } //a is an inbound invitation
                        if (!invitationB) { //b is an inbound game
                            return 1 //b < a
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





