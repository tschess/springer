package io.bahlsenwitz.springer.controller.player.request

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerRefresh(private val repositoryPlayer: RepositoryPlayer) {

    data class RequestRefresh(val id_player: String, val size: Int)

    fun refresh(requestRefresh: RequestRefresh): ResponseEntity<Any> {
        val refreshList: MutableList<Player> = mutableListOf()
        repositoryPlayer.findAll().sorted().subList(0, requestRefresh.size).forEach {
            refreshList.add(it)
        }
        val player: Player = repositoryPlayer.findById(UUID.fromString(requestRefresh.id_player)!!).get()
        refreshList.add(player) //easier to remove at end...
        return ResponseEntity.ok(refreshList)
    }

}