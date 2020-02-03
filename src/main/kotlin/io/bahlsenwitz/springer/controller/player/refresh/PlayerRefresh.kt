package io.bahlsenwitz.springer.controller.player.refresh

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerRefresh(private val repositoryPlayer: RepositoryPlayer) {

    fun refresh(requestRefresh: RequestRefresh): ResponseEntity<Any> {

        val uuid: UUID = UUID.fromString(requestRefresh.id_player)!!
        val player: Player = repositoryPlayer.findById(uuid).get()

        val size: Int = requestRefresh.page_size + 1

        val playerList: List<Player> = repositoryPlayer.findAll().sorted().subList(0, size)

        val playerRefreshCore: PlayerRefreshCore = PlayerRefreshCore(player = player, playerList = playerList)
        return ResponseEntity.ok(playerRefreshCore)
    }

    data class RequestRefresh(
        val id_player: String,
        val page_size: Int
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