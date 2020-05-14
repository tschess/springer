package io.bahlsenwitz.springer.controller.game.menu

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.controller.Output
import org.springframework.http.ResponseEntity
import java.util.*

class GameMenu(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val output: Output =
        Output()

    data class RequestMenu(
        val id: String,
        val index: Int,
        val size: Int,
        val self: Boolean
    )

    fun menu(requestActual: RequestMenu): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(requestActual.id)!!
        val player: Player = repositoryPlayer.findById(uuid).get()
        player.note = false
        repositoryPlayer.save(player)

        val playerList: List<Game> = repositoryGame.findPlayerList(uuid)
        if (playerList.isEmpty()) {
            return output.terminal(result = "eol", route = "menu")
        }
        val self: Boolean = requestActual.self
        val playerListFilter: List<Game>
        playerListFilter = if (self) {
            playerList.filter {
                getSelf(it)
            }
        } else {
            playerList.filter {
                getOther(it)
            }
        }
        val gameList: List<Game>

        val pageIndex: Int = requestActual.index
        val pageSize: Int = requestActual.size

        val indexFrom: Int = pageIndex * pageSize
        val indexTo: Int = indexFrom + pageSize

        if (playerListFilter.lastIndex < indexFrom) {
            return ResponseEntity.ok(ResponseEntity.EMPTY)
        }
        if (playerListFilter.lastIndex + 1 <= indexTo) {
            gameList = playerListFilter.sortedWith(Game).subList(indexFrom, playerListFilter.lastIndex + 1)
            return ResponseEntity.ok(gameList)
        }
        gameList = playerListFilter.sortedWith(Game).subList(indexFrom, indexTo + 1)
        return ResponseEntity.ok(gameList)
    }

    private fun getOther(game: Game): Boolean {
        if (game.status == STATUS.RESOLVED &&
            game.condition != CONDITION.REFUSED &&
            game.condition != CONDITION.RESCIND &&
            game.condition != CONDITION.TIMEOUT &&
            game.condition != CONDITION.EXPIRED
        ) {
            return true
        }
        return false
    }

    private fun getSelf(game: Game): Boolean {
        if (game.status == STATUS.PROPOSED || game.status == STATUS.ONGOING || getOther(game)) {
            return true
        }
        return false
    }

}





