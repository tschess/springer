package io.bahlsenwitz.springer.controller.player.init

import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class PlayerInit(private val repositoryPlayer: RepositoryPlayer) {

    fun device(device: String): ResponseEntity<Any> {
        val player = repositoryPlayer.getByDevice(device)
            ?: return ResponseEntity.status(HttpStatus.OK).body("{\"info\": \"unassigned\"}")
        return ResponseEntity.ok(player)
    }
}