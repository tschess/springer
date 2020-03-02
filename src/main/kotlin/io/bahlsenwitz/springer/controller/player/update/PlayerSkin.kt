package io.bahlsenwitz.springer.controller.player.update

import io.bahlsenwitz.springer.controller.game.menu.invite.GameAck
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.SKIN
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Constant
import org.springframework.http.ResponseEntity
import java.time.ZonedDateTime
import java.util.*

class PlayerSkin(private val repositoryPlayer: RepositoryPlayer) {

    fun skin(updateSkin: UpdateSkin): ResponseEntity<Player> {
        val uuid: UUID = UUID.fromString(updateSkin.id)!!
        val player: Player = repositoryPlayer.findById(uuid).get()

        val acquisition: List<SKIN> = listOf(SKIN.valueOf(updateSkin.skin))
        player.skin = player.skin + acquisition

        player.updated = Constant().getDate()
        khttp.post(url = "${Constant().INFLUX}write?db=tschess", data = "activity player=\"${player.id}\",route=\"skin\"")
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    data class UpdateSkin (
        val skin: String,
        val id: String
    )
}