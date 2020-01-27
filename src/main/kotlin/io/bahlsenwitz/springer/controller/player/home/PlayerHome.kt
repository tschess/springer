package io.bahlsenwitz.springer.controller.player.home

import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class PlayerHome(private val repositoryPlayer: RepositoryPlayer) {

    fun leaderboard(page: Int): ResponseEntity<Any> {
        val playerListAll: List<Player> = repositoryPlayer.findAll(Sort.by("elo").descending())
        val opponentPage: MutableList<Opponent> = mutableListOf()

        val PAGE_SIZE = 9

        val indexFrom: Int = page * PAGE_SIZE
        val indexTo: Int = indexFrom + PAGE_SIZE

        if(playerListAll.lastIndex < indexFrom) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"leaderboard\": \"EOL\"}")
        }
        if(playerListAll.lastIndex + 1 <= indexTo) {

            val xxx = playerListAll.subList(indexFrom, playerListAll.lastIndex + 1)
            for (item in xxx) {
                val opponent = Opponent(
                    id = item.id.toString(),
                    username = item.username,
                    avatar = item.avatar,
                    elo = item.elo,
                    rank = item.rank,
                    date = item.date,
                    disp = item.disp
                )
                opponentPage.add(opponent)
            }
        } else {
            val xxx = playerListAll.subList(indexFrom, indexTo + 1)
            for (item in xxx) {
                val opponent = Opponent(
                    id = item.id.toString(),
                    username = item.username,
                    avatar = item.avatar,
                    elo = item.elo,
                    rank = item.rank,
                    date = item.date,
                    disp = item.disp
                )
                opponentPage.add(opponent)
            }
        }
        return ResponseEntity.ok(opponentPage)
    }

    data class Opponent(
        val id: String,
        val username: String,
        val avatar: String,
        val elo: Int,
        val rank: Int,
        val date: String,
        val disp: Int)

}

