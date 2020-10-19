package io.bahlsenwitz.springer.controller.player.path

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerRivals(private val repositoryPlayer: RepositoryPlayer) {

    fun rivals(id: String): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(id)!!).get()
        val listHead: List<Player> = repositoryPlayer.findAll().filter { it.id != player.id }.sorted().take(11)
        val listRivals: List<Player> = listHead.shuffled().take(3)
        return ResponseEntity.ok().body(listRivals)
    }

}