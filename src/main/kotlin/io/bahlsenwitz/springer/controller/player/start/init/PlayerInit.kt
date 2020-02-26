package io.bahlsenwitz.springer.controller.player.start.init

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Constant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class PlayerInit(private val repositoryPlayer: RepositoryPlayer) {

//     TODO: Refresh (minimize then reopen the app...)...
//    fun refresh(id: String): ResponseEntity<Any> {
//        return ResponseEntity.badRequest().body("{\"TODO\": \"IMPLEMENT_ME\"}")
//    }
//
//    @PostMapping("/refresh/{id}")
//    fun refresh(@PathVariable(value = "id") id: String): ResponseEntity<Any> {
//        return playerInit.refresh(id)
//    }

    fun device(device: String): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findByDevice(device)
            ?: return ResponseEntity.status(HttpStatus.OK).body("{\"info\": \"unassigned\"}")
        khttp.post(url = "${Constant().INFLUX}write?db=tschess", data = "activity player=\"${player.id}\",route=\"device\"")
        return ResponseEntity.ok(player)
    }
}