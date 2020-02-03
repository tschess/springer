package io.bahlsenwitz.springer.controller.player.home

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

//print("\n\n")
//print("                  indexFrom: ${indexFrom}\n")
//print("                    indexTo: ${indexTo}\n")
//print("playerListFindAll.lastIndex: ${playerListFindAll.lastIndex}\n")
//print("\n\n")
//curl --header "Content-Type: application/json" --request POST --data '{"index": 0, "size": 9}' http://localhost:8080/player/leaderboard
class PlayerHome(private val repositoryPlayer: RepositoryPlayer) {

    fun leaderboard(requestPage: RequestPage): ResponseEntity<Any> {
        val testList: MutableList<Int> = mutableListOf()

        val playerListFindAll: List<Player> = repositoryPlayer.findAll().sorted()
        val opponentPlayerList: List<Player>

        val pageList: MutableList<Player.Core> = mutableListOf()

        val pageIndex: Int = requestPage.index
        val pageSize: Int = requestPage.size

        val indexFrom: Int = pageIndex * pageSize
        val indexTo: Int = indexFrom + pageSize

        if (playerListFindAll.lastIndex <= indexFrom) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"leaderboard\": \"EOL\"}")
        }
        if (playerListFindAll.lastIndex <= indexTo) {
            opponentPlayerList = playerListFindAll.subList(indexFrom, playerListFindAll.lastIndex + 1)
            for (opponentPlayer: Player in opponentPlayerList) {
                val opponent = Player.Core(opponentPlayer)
                pageList.add(opponent)
                testList.add(opponentPlayer.rank)
            }
            //return ResponseEntity.ok(testList)
            return ResponseEntity.ok(pageList)
        }
        opponentPlayerList = playerListFindAll.subList(indexFrom, indexTo)
        for (opponentPlayer: Player in opponentPlayerList) {
            val opponent = Player.Core(opponentPlayer)
            pageList.add(opponent)
            testList.add(opponentPlayer.rank)
        }
        //return ResponseEntity.ok(testList)
        return ResponseEntity.ok(pageList)
    }

    data class RequestPage(
        val index: Int,
        val size: Int
    )
}

