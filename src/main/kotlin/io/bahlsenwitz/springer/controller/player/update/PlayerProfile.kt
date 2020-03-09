package io.bahlsenwitz.springer.controller.player.update

import io.bahlsenwitz.springer.influx.Influx
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerProfile(private val repositoryPlayer: RepositoryPlayer) {

    fun clear(device: String): ResponseEntity<Any> {
        val player: Player? = repositoryPlayer.findByDevice(device)
        if(player != null){
            player.device = null
           // khttp.post(url = "${DateTime().INFLUX}write?db=tschess", data = "activity player=\"${player.id}\",route=\"clear\"")
            Influx().activity(player_id = player.id.toString(), route = "clear")
            return ResponseEntity.ok(repositoryPlayer.save(player))
        }
        return ResponseEntity.ok("{\"result\": \"ok\"}")
    }

    fun avatar(updateAvatar: UpdateAvatar): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(updateAvatar.id)!!
        val player: Player = repositoryPlayer.findById(uuid).get()
        player.avatar = updateAvatar.avatar

        //khttp.post(url = "${DateTime().INFLUX}write?db=tschess", data = "activity player=\"${player.id}\",route=\"avatar\"")
        Influx().activity(player_id = player.id.toString(), route = "avatar")
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    data class UpdateAvatar (
        val id: String,
        val avatar: String
    )
}

