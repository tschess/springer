package io.bahlsenwitz.springer.controller.game.actual

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.GameCoreActual
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTimeGenerator
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
        val playerListSort: List<Game> = playerListFilter.sortedWith(ComparatorActual)

        val gameCoreActualList: MutableList<GameCoreActual> = mutableListOf()
        val gameList: List<Game>

        val pageIndex: Int = requestActual.index
        val pageSize: Int = requestActual.size

        val indexFrom: Int = pageIndex * pageSize
        val indexTo: Int = indexFrom + pageSize

        if (playerListSort.lastIndex < indexFrom) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"actual\": \"EOL\"}")
        }
        if (playerListSort.lastIndex + 1 <= indexTo) {
            gameList = playerListSort.subList(indexFrom, playerListSort.lastIndex + 1)
            for (game: Game in gameList) {
                val gameCoreActual = GameCoreActual(player = player, game = game)
                gameCoreActualList.add(gameCoreActual)
            }
            return ResponseEntity.ok(gameCoreActualList)
        }
        gameList = playerListSort.subList(indexFrom, indexTo + 1)
        for (game: Game in gameList) {
            val gameCoreActual = GameCoreActual(player = player, game = game)
            gameCoreActualList.add(gameCoreActual)
        }
        return ResponseEntity.ok(gameCoreActualList)
    }

    data class RequestActual(
        val id: String,
        val index: Int,
        val size: Int
    )

    class ComparatorActual {

        companion object : Comparator<Game> {

            private val DATE_TIME_GENERATOR = DateTimeGenerator()

            override fun compare(a: Game, b: Game): Int {
                val statusA: STATUS = a.status
                val statusB: STATUS = b.status

                if (statusA == STATUS.PROPOSED) {
                    if (statusB == STATUS.PROPOSED) {
                        val createdA: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = a.date_create)
                        val createdB: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = b.date_create)

                        val createdAB: Boolean = createdA.isBefore(createdB)
                        if (createdAB) {
                            return -1
                        }
                        return 1
                    } //b is going...
                    return -1
                } //for sure a is ongoing... gotta check if also is b...
                if (statusB == STATUS.ONGOING) {
                    val updatedA: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = a.date_update)
                    val updatedB: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = b.date_update)

                    val updatedAB: Boolean = updatedA.isBefore(updatedB)
                    if (updatedAB) {
                        return -1
                    }
                    return 1
                }
                return 1
            }
        }
    }
}


