package io.bahlsenwitz.springer.controller.player.request

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.controller.Output
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerConfig(private val repositoryPlayer: RepositoryPlayer) {

    private val output: Output =
        Output(repositoryPlayer)

    data class UpdateConfig(
        val config: List<List<String>>,
        val index: Int,
        val id: String
    )

    fun config(updateConfig: UpdateConfig): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(updateConfig.id)!!).get()
        if (updateConfig.index == 0) {
            player.config0 = updateConfig.config
        }
        if (updateConfig.index == 1) {
            player.config1 = updateConfig.config
        }
        if (updateConfig.index == 2) {
            player.config2 = updateConfig.config
        }
        return output.player(player = player, route = "config")
    }

}