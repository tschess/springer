package io.bahlsenwitz.springer.controller.game.actual

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.GameCoreActual
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.ZonedDateTime
import java.util.*

class GameActual(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun actual(requestActual: RequestActual): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(requestActual.id)!!
        val player: Player = repositoryPlayer.getById(uuid)
        val playerList: List<Game> = repositoryGame.getPlayerList(uuid)
        val playerListFilter: List<Game> =
            playerList.filter { it.status == STATUS.PROPOSED || it.status == STATUS.ONGOING }

        val gameCoreActualList: MutableList<GameCoreActual> = mutableListOf()
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
                val gameCoreActual = GameCoreActual(player = player, game = game)
                gameCoreActualList.add(gameCoreActual)
            }
            val gameCoreActualSort: List<GameCoreActual> = gameCoreActualList.sortedWith(ComparatorGameCoreActual)
            return ResponseEntity.ok(gameCoreActualSort)
        }
        gameList = playerListFilter.subList(indexFrom, indexTo + 1)
        for (game: Game in gameList) {
            val gameCoreActual = GameCoreActual(player = player, game = game)
            gameCoreActualList.add(gameCoreActual)
        }
        val gameCoreActualSort: List<GameCoreActual> = gameCoreActualList.sortedWith(ComparatorGameCoreActual)
        return ResponseEntity.ok(gameCoreActualSort)
    }

    data class RequestActual(
        val id: String,
        val index: Int,
        val size: Int
    )

    class ComparatorGameCoreActual {

        companion object : Comparator<GameCoreActual> {

            private val DATE_TIME_GENERATOR = GeneratorDateTime()

            override fun compare(a: GameCoreActual, b: GameCoreActual): Int {
                val inboundA: Boolean = a.inbound
                val inboundB: Boolean = b.inbound
                val invitationA: Boolean = a.invitation
                val invitationB: Boolean = b.invitation
                val updateA: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = a.date)
                val updateB: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = b.date)
                val updateAB: Boolean = updateA.isBefore(updateB)
                if(inboundA) { //a in
                    if(inboundB) { //b in
                        if(!invitationA) { //a game
                            if(!invitationB) { //b game
                                if (updateAB) {
                                    return -1 //a < b
                                }
                                return 1 //b < a
                            } //a is an inbound game, b is an inbound invitation
                            return -1 //a < b
                        } //a is an inbound invitation
                        if(!invitationB) { //b is an inbound game
                            return 1 //b < a
                        }
                    } // b is outbound, a is inbound
                    return -1 //a < b
                } //a is outbound
                if(!inboundB){ //b is outbound
                    if(!invitationA) { //a is game
                        if(!invitationB) { //b is game
                            if (updateAB) {
                                return -1 //a < b
                            }
                            return 1 //b < a
                        } //b is invite
                        return -1 //a < b
                    } //a is invite
                    if(!invitationB) { //b is game
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


