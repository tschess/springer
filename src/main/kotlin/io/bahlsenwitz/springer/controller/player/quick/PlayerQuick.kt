package io.bahlsenwitz.springer.controller.player.quick

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerQuick(private val repositoryPlayer: RepositoryPlayer) {

    //
    //TODO: need also consider how many ongoing games they have, if we have a mutual ongoing game, etc...
    //
    fun quick(id: String): ResponseEntity<Player> {
        val uuid: UUID = UUID.fromString(id)!!
        val playerListTopTen: List<Player> = repositoryPlayer.findAll().filter { it.id != uuid }.sorted().take(11)
        val opponent: Player = playerListTopTen.shuffled().take(1)[0]
        return ResponseEntity.ok(opponent)
    }

}