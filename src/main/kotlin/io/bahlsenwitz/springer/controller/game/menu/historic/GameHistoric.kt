package io.bahlsenwitz.springer.controller.game.menu.historic

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.OUTCOME
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class GameHistoric(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun historic(requestHistoric: RequestHistoric): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(requestHistoric.id)!!
        val player: Player = repositoryPlayer.findById(uuid).get()
        val playerList: List<Game> = repositoryGame.findPlayerList(uuid)
        val playerListResolved: List<Game> = playerList.filter {
            it.status == STATUS.RESOLVED && it.outcome != OUTCOME.REFUSED && it.outcome != OUTCOME.RESCIND
        }

        val playerListResolvedSorted: List<Game> = playerListResolved.sortedWith(ComparatorHistoric)

        val gameCoreHistoricList: MutableList<Game> = mutableListOf()
        val gameList: List<Game>

        val pageIndex: Int = requestHistoric.index
        val pageSize: Int = requestHistoric.size

        val indexFrom: Int = pageIndex * pageSize
        val indexTo: Int = indexFrom + pageSize

        if (playerListResolvedSorted.lastIndex < indexFrom) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"historic\": \"EOL\"}")
        }
        if (playerListResolvedSorted.lastIndex + 1 <= indexTo) {
            gameList = playerListResolvedSorted.subList(indexFrom, playerListResolvedSorted.lastIndex + 1)
            for (game: Game in gameList) {
                //val gameCore = GameCoreHistoric(player = player, game = game)
                gameCoreHistoricList.add(game)
            }
            return ResponseEntity.ok(gameCoreHistoricList)
        }
        gameList = playerListResolvedSorted.subList(indexFrom, indexTo + 1)
        for (game: Game in gameList) {
            //val gameCore = GameCoreHistoric(player = player, game = game)
            gameCoreHistoricList.add(game)
        }
        return ResponseEntity.ok(gameCoreHistoricList)
    }

    data class RequestHistoric(
        val id: String,
        val index: Int,
        val size: Int
    )

    class ComparatorHistoric {

        companion object : Comparator<Game> {

            override fun compare(a: Game, b: Game): Int {
                val dateA: ZonedDateTime = a.updated.toInstant().atZone(ZoneId.of("America/New_York"))
                val dateB: ZonedDateTime = b.updated.toInstant().atZone(ZoneId.of("America/New_York"))
                if (dateA.isBefore(dateB)) {
                    return 1
                }
                return -1
            }
        }
    }
}