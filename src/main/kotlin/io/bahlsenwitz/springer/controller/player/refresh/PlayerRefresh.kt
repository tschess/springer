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
        val refreshList: MutableList<Player> = mutableListOf()

        val uuid: UUID = UUID.fromString(requestRefresh.id_player)!!
        val player: Player = repositoryPlayer.findById(uuid).get()
        refreshList.add(player)

        val size: Int = requestRefresh.size
        repositoryPlayer.findAll().sorted().subList(0, size).forEach { refreshList.add(it) }

        return ResponseEntity.ok(refreshList)
    }

    data class RequestRefresh(
        val id_player: String,
        val size: Int
    )
}