package io.bahlsenwitz.springer.controller.player.path

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.util.HashMap

class PlayerInit(private val repositoryPlayer: RepositoryPlayer) {

    fun device(device: String): ResponseEntity<Any> {
        val player: Player? = repositoryPlayer.findByDevice(device)
        if(player == null){
            val body: MutableMap<String, String> = HashMap()
            body["fail"] = "device"
            return ResponseEntity.ok().body(body)
        }
        player.updated = DateTime().getDate()
        return ResponseEntity.ok().body(repositoryPlayer.save(player))
    }
}