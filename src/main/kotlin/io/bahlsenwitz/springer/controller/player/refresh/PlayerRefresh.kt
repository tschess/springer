package io.bahlsenwitz.springer.controller.player.refresh

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

//val testList: MutableList<Int> = mutableListOf()
//for (playerX: Player in playerList) {
//    testList.add(playerX.rank)
//}
//return ResponseEntity.ok(testList)
//curl --header "Content-Type: application/json" --request POST --data '{"id_player": "99999999-9999-9999-9999-999999999999", "size": 9}' http://localhost:8080/player/refresh
class PlayerRefresh(private val repositoryPlayer: RepositoryPlayer) {

    fun refresh(requestRefresh: RequestRefresh): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(requestRefresh.id_player)!!
        val player: Player = repositoryPlayer.findById(uuid).get()

        val size: Int = requestRefresh.size
        val playerList: List<Player> = repositoryPlayer.findAll().sorted().subList(0, size)

        val playerRefreshCore: PlayerRefreshCore = PlayerRefreshCore(player = player, playerList = playerList)
        return ResponseEntity.ok(playerRefreshCore)
    }

    data class RequestRefresh(
        val id_player: String,
        val size: Int
    )

    class PlayerRefreshCore(player: Player, playerList: List<Player>) {
        private val info: Info = getInfo(playerList)

        val elo: Int = player.elo
        val rank: Int = player.rank
        val disp: Int = player.disp
        val page: List<Player.Core> = info.page

        companion object {
            data class Info(val page: List<Player.Core>)

            fun getInfo(playerList: List<Player>): Info {
                val pageList: MutableList<Player.Core> = mutableListOf()
                for (player0: Player in playerList) {
                    val core = Player.Core(player0)
                    pageList.add(core)
                }
                return Info(pageList)
            }
        }
    }
}