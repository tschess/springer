package io.bahlsenwitz.springer.controller.player.request

import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.time.ZonedDateTime
import java.util.*

class PlayerHome(private val repositoryPlayer: RepositoryPlayer) {

    private val output: Output = Output()
    private val dateTime: DateTime = DateTime()

    data class RequestPage(val index: Int, val size: Int)

    fun leaderboard(requestPage: RequestPage): ResponseEntity<Any> {
        val playerListFindAll: List<Player> = this.getActiveList()

        val playerList: List<Player>
        val pageList: MutableList<Player> = mutableListOf()

        val pageIndex: Int = requestPage.index
        val pageSize: Int = requestPage.size

        val indexFrom: Int = pageIndex * pageSize
        val indexTo: Int = indexFrom + pageSize

        if (playerListFindAll.lastIndex <= indexFrom) {
            return output.terminal(result = "eol", route = "leaderboard")
        }
        if (playerListFindAll.lastIndex <= indexTo) {
            playerList = playerListFindAll.subList(indexFrom, playerListFindAll.lastIndex + 1)
            for (playerX: Player in playerList) {
                pageList.add(playerX)
            }
            return ResponseEntity.ok(pageList)
        }
        playerList = playerListFindAll.subList(indexFrom, indexTo)
        for (playerX: Player in playerList) {
            pageList.add(playerX)
        }
        return ResponseEntity.ok(pageList)
    }

    private fun getActive(player: Player): Boolean {
        val time00: ZonedDateTime = dateTime.rn().minusDays(7L)
        val time01: ZonedDateTime = dateTime.getDate(player.updated)
        if (time00.isBefore(time01)) {
            return true
        }
        return false
    }

    private fun getActiveList(): List<Player> {
        return  repositoryPlayer.findAll().filter {
            this.getActive(it)
        }.sorted()
    }

    fun rivals(id: String): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(id)!!).get()
        val listRivals: List<Player> = this.getActiveList().filter { it.id != player.id }.shuffled().take(3)
        return ResponseEntity.ok().body(listRivals)
    }
}

