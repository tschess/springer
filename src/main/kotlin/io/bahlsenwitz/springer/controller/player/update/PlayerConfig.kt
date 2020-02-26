package io.bahlsenwitz.springer.controller.player.update

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Constant
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerConfig(private val repositoryPlayer: RepositoryPlayer) {

    fun config(updateConfig: UpdateConfig): ResponseEntity<Player> {
        val uuid: UUID = UUID.fromString(updateConfig.id)!!
        val player: Player = repositoryPlayer.findById(uuid).get()
        if(updateConfig.index == 0){
            player.config0 = updateConfig.config
        }
        if(updateConfig.index == 1){
            player.config1 = updateConfig.config
        }
        if(updateConfig.index == 2){
            player.config2 = updateConfig.config
        }
        player.updated = Constant().getDate()
        khttp.post(url = "${Constant().INFLUX}write?db=tschess", data = "activity player=\"${player.id}\",route=\"config\"")
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    data class UpdateConfig (
        val config: List<List<String>>,
        val index: Int,
        val id: String
    )
}