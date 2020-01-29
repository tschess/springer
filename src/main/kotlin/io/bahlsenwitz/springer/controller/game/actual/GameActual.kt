package io.bahlsenwitz.springer.controller.game.actual

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.GameCoreActual
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTimeGenerator
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
        val playerListResolved: List<Game> = playerList.filter { it.status == STATUS.PROPOSED || it.status == STATUS.ONGOING }
        val playerListResolvedSorted: List<Game> = playerListResolved.sortedWith(ComparatorActual)

        val gameCoreActualList: MutableList<GameCoreActual> = mutableListOf()
        val gameList: List<Game>

        val pageIndex: Int = requestActual.index
        val pageSize: Int = requestActual.size

        val indexFrom: Int = pageIndex * pageSize
        val indexTo: Int = indexFrom + pageSize

        return ResponseEntity.ok("")
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
                val dateA: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = a.date_update)
                val dateB: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = b.date_update)
                if (dateA.isBefore(dateB)) {
                    return -1
                }
                return 1
            }
        }
    }
}


