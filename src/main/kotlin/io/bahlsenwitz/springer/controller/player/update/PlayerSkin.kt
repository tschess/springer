package io.bahlsenwitz.springer.controller.player.update

import io.bahlsenwitz.springer.model.game.SKIN
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class PlayerSkin(private val repositoryPlayer: RepositoryPlayer) {

    fun skin(updateSkin: UpdateSkin): ResponseEntity<Player> {
        val uuid: UUID = UUID.fromString(updateSkin.id)!!
        val player: Player = repositoryPlayer.findById(uuid).get()

        val acquisition: List<SKIN> = listOf(SKIN.valueOf(updateSkin.skin))
        player.skin = player.skin + acquisition

        player.updated = Date.from(ZonedDateTime.now(ZoneId.of("America/New_York")).toInstant())
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    data class UpdateSkin (
        val skin: String,
        val id: String
    )
}