package io.bahlsenwitz.springer.controller.player.home

import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class PlayerHome(private val repositoryPlayer: RepositoryPlayer) {

    fun leaderboard(requestPage: RequestPage): ResponseEntity<Any> {
        val playerListFindAll: List<Player> = repositoryPlayer.findAll().sorted()
        val opponentPageList: MutableList<ReturnOpponent> = mutableListOf()
        val opponentPlayerList: List<Player>

        val pageIndex: Int = requestPage.index
        val pageSize: Int = requestPage.size

        val indexFrom: Int = pageIndex * pageSize
        val indexTo: Int = indexFrom + pageSize

        if (playerListFindAll.lastIndex < indexFrom) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"leaderboard\": \"EOL\"}")
        }
        if (playerListFindAll.lastIndex + 1 <= indexTo) {
            opponentPlayerList = playerListFindAll.subList(indexFrom, playerListFindAll.lastIndex + 1)
            for (opponentPlayer: Player in opponentPlayerList) {
                val opponentReturn = ReturnOpponent(
                    id = opponentPlayer.id.toString(),
                    username = opponentPlayer.username,
                    avatar = opponentPlayer.avatar,
                    elo = opponentPlayer.elo,
                    rank = opponentPlayer.rank,
                    date = opponentPlayer.date,
                    disp = opponentPlayer.disp
                )
                opponentPageList.add(opponentReturn)
            }
            return ResponseEntity.ok(opponentPageList)
        }
        opponentPlayerList = playerListFindAll.subList(indexFrom, indexTo + 1)
            for (opponentPlayer: Player in opponentPlayerList) {
                val opponentReturn = ReturnOpponent(
                    id = opponentPlayer.id.toString(),
                    username = opponentPlayer.username,
                    avatar = opponentPlayer.avatar,
                    elo = opponentPlayer.elo,
                    rank = opponentPlayer.rank,
                    date = opponentPlayer.date,
                    disp = opponentPlayer.disp
                )
                opponentPageList.add(opponentReturn)
            }
        return ResponseEntity.ok(opponentPageList)
    }

    data class RequestPage(
        val index: Int,
        val size: Int
    )

    data class ReturnOpponent(
        val id: String,
        val username: String,
        val avatar: String,
        val elo: Int,
        val rank: Int,
        val date: String,
        val disp: Int
    )

}

