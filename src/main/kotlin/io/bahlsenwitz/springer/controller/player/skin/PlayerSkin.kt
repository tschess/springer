package io.bahlsenwitz.springer.controller.player.skin

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.game.SKIN
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerSkin(private val repositoryPlayer: RepositoryPlayer) {

    private val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun skin(updateSkin: UpdateSkin): ResponseEntity<Player> {
        val uuid: UUID = UUID.fromString(updateSkin.id)!!
        val player: Player = repositoryPlayer.findById(uuid).get()

        val acquisition: List<SKIN> = listOf(SKIN.valueOf(updateSkin.skin))
        player.skin_list = player.skin_list + acquisition

        player.updated = DATE_TIME_GENERATOR.rightNowString()
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    data class UpdateSkin (
        val skin: String,
        val id: String
    )
}