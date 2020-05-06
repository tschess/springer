package io.bahlsenwitz.springer.controller.player.board

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class PlayerHome(private val repositoryPlayer: RepositoryPlayer) {

    data class RequestPage(val index: Int, val size: Int)

    fun leaderboard(requestPage: RequestPage): ResponseEntity<Any> {
        val playerListFindAll: List<Player> = repositoryPlayer.findAll().sorted()
        val playerList: List<Player>
        val pageList: MutableList<Player> = mutableListOf()

        val pageIndex: Int = requestPage.index
        val pageSize: Int = requestPage.size

        val indexFrom: Int = pageIndex * pageSize
        val indexTo: Int = indexFrom + pageSize

        if (playerListFindAll.lastIndex <= indexFrom) {
            return ResponseEntity.ok(ResponseEntity.noContent())
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
}

