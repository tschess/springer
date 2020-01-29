package io.bahlsenwitz.springer.controller.player.config

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerConfig(private val repositoryPlayer: RepositoryPlayer) {

    fun config(updateConfig: UpdateConfig): ResponseEntity<Player> {
        val id = UUID.fromString(updateConfig.id)!!
        val player = repositoryPlayer.getById(id)
        if(updateConfig.index == 0){
            player.config0 = updateConfig.config
        }
        if(updateConfig.index == 1){
            player.config1 = updateConfig.config
        }
        if(updateConfig.index == 2){
            player.config2 = updateConfig.config
        }
        player.updated = updateConfig.updated
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    data class UpdateConfig (
        val config: List<List<String>>,
        val index: Int,
        val id: String,
        val updated: String
    )

}