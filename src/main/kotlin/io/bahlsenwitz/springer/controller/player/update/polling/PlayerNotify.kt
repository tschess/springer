package io.bahlsenwitz.springer.controller.player.update.polling

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerNotify(private val repositoryPlayer: RepositoryPlayer) {

    //curl --header "Content-Type: application/json" --request GET http://localhost:8080/player/notify/00000000-0000-0000-0000-000000000000
    fun notify(id: String): Any {
        val uuid: UUID = UUID.fromString(id)!!
        val player: Player = repositoryPlayer.findById(uuid).get()
        if(player.notify){
            return ResponseEntity.status(HttpStatus.OK).body("{\"notify\":${true}}")
        }
        return ResponseEntity.EMPTY
    }
}