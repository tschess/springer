package io.bahlsenwitz.springer.controller.game.historic

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.GameCoreHistoric
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTimeGenerator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.ZonedDateTime
import java.util.*

class GameHistoric(private val repositoryGame: RepositoryGame,
                   private val repositoryPlayer: RepositoryPlayer) {

    fun historic(requestOther: RequestHistoric): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(requestOther.id)!!
        val other: Player = repositoryPlayer.getById(uuid)
        val playerList: List<Game> = repositoryGame.getPlayerList(uuid)
        val playerListResolved: List<Game> = playerList.filter { it.status == STATUS.RESOLVED }
        val playerListResolvedSorted: List<Game> = playerListResolved.sortedWith(ComparatorHistoric)

        val gameListCore: MutableList<GameCoreHistoric> = mutableListOf()
        val gameList: List<Game>

        val pageIndex: Int = requestOther.index
        val pageSize: Int = requestOther.size

        val indexFrom: Int = pageIndex * pageSize
        val indexTo: Int = indexFrom + pageSize

        if (playerListResolvedSorted.lastIndex < indexFrom) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"other\": \"EOL\"}")
        }
        if (playerListResolvedSorted.lastIndex + 1 <= indexTo) {
            gameList = playerListResolvedSorted.subList(indexFrom, playerListResolvedSorted.lastIndex + 1)
            for (game: Game in gameList) {
                val gameCore = GameCoreHistoric(player = other, game = game)
                gameListCore.add(gameCore)
            }
            return ResponseEntity.ok(gameListCore)
        }
        gameList = playerListResolvedSorted.subList(indexFrom, indexTo + 1)
        for (game: Game in gameList) {
            val gameCore = GameCoreHistoric(player = other, game = game)
            gameListCore.add(gameCore)
        }
        return ResponseEntity.ok(gameListCore)
    }

    data class RequestHistoric(
        val id: String,
        val index: Int,
        val size: Int
    )

    class ComparatorHistoric {

        companion object: Comparator<Game> {

            private val DATE_TIME_GENERATOR = DateTimeGenerator()

            override fun compare(a: Game, b: Game): Int {
                val dateA: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = a.date_end)
                val dateB: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = b.date_end)
                if (dateA.isBefore(dateB)) {
                    return -1
                }
                return 1
            }
        }
    }
}