package io.bahlsenwitz.springer.controller.game.menu

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import java.util.*

class GameMenu(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private fun getOther(game: Game): Boolean {
        if( game.status == STATUS.RESOLVED &&
            game.condition != CONDITION.REFUSED &&
            game.condition != CONDITION.RESCIND &&
            game.condition != CONDITION.TIMEOUT &&
            game.condition != CONDITION.EXPIRED){
            return true
        }
        return false
    }

    private fun getSelf(game: Game): Boolean {
        if(game.status == STATUS.PROPOSED || game.status == STATUS.ONGOING || getOther(game)){
            return true
        }
        return false
    }

    fun menu(requestActual: RequestMenu): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(requestActual.id)!!
        val player: Player = repositoryPlayer.findById(uuid).get()
        player.note = false
        repositoryPlayer.save(player)
        //khttp.post(url = "${DateTime().INFLUX}write?db=tschess", data = "menu id=\"${player.id}\",route=\"menu\"")
        Influx().activity(player_id = player.id.toString(), route = "menu")

        val playerList: List<Game> = repositoryGame.findPlayerList(uuid)
        if (playerList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"zero\":${true}}")
        }
        val self: Boolean = requestActual.self
        val playerListFilter: List<Game>
        playerListFilter = if(self){
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
            return ResponseEntity.status(HttpStatus.OK).body("{\"eol\":${true}}")
        }
        if (playerListFilter.lastIndex + 1 <= indexTo) {
            gameList = playerListFilter.sortedWith(Game).subList(indexFrom, playerListFilter.lastIndex + 1)
            return ResponseEntity.ok(gameList)
        }
        gameList = playerListFilter.sortedWith(Game).subList(indexFrom, indexTo + 1)
        return ResponseEntity.ok(gameList)
    }

    data class RequestMenu(
        val id: String,
        val index: Int,
        val size: Int,
        val self: Boolean
    )


}





