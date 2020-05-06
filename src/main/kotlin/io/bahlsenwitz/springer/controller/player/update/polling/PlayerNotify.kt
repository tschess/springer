package io.bahlsenwitz.springer.controller.player.update.polling

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerNotify(private val repositoryPlayer: RepositoryPlayer) {

    fun notify(id: String): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(id)!!).get()
        if(player.note){
            //return ResponseEntity.status(HttpStatus.OK).body("{\"notify\":${true}}")
            return ResponseEntity.ok(ResponseEntity.ok())
        }
        return ResponseEntity.ok(ResponseEntity.noContent())
    }
}