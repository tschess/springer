package io.bahlsenwitz.springer.controller.player.update

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
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
        player.updated = DateTime().getDate()
        //khttp.post(url = "${DateTime().INFLUX}write?db=tschess", data = "activity player=\"${player.id}\",route=\"config\"")
        Influx().activity(player_id = player.id.toString(), route = "config")
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    data class UpdateConfig (
        val config: List<List<String>>,
        val index: Int,
        val id: String
    )
}