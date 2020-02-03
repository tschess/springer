package io.bahlsenwitz.springer.controller.player.home

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class PlayerHome(private val repositoryPlayer: RepositoryPlayer) {

    fun leaderboard(requestPage: RequestPage): ResponseEntity<Any> {
        val playerListFindAll: List<Player> = repositoryPlayer.findAll().sorted()
        val opponentPageList: MutableList<Player.Core> = mutableListOf()
        val opponentPlayerList: List<Player>

        val pageIndex: Int = requestPage.index
        val pageSize: Int = requestPage.size

        var indexFrom: Int = pageIndex * pageSize
        if(indexFrom > 0){
            indexFrom -= 1
        }
        val indexTo: Int = indexFrom + pageSize

        if (playerListFindAll.lastIndex < indexFrom) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"leaderboard\": \"EOL\"}")
        }
        if (playerListFindAll.lastIndex + 1 <= indexTo) {
            opponentPlayerList = playerListFindAll.subList(indexFrom, playerListFindAll.lastIndex + 1)
            for (opponentPlayer: Player in opponentPlayerList) {
                val opponent = Player.Core(opponentPlayer)
                opponentPageList.add(opponent)
            }
            return ResponseEntity.ok(opponentPageList)
        }
        opponentPlayerList = playerListFindAll.subList(indexFrom, indexTo)
            for (opponentPlayer: Player in opponentPlayerList) {
                val opponent = Player.Core(opponentPlayer)
                opponentPageList.add(opponent)
            }
        return ResponseEntity.ok(opponentPageList)
    }

    data class RequestPage(
        val index: Int,
        val size: Int
    )
}

