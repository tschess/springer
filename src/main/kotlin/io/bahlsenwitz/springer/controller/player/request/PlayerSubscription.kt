package io.bahlsenwitz.springer.controller.player.request

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerSubscription(private val repositoryPlayer: RepositoryPlayer) {

    data class UpdateSubscription(
        val id: String,
        val date: String
    )

    fun subscription(updateSubscription: UpdateSubscription): ResponseEntity<Any> {
        val player: Player = repositoryPlayer.findById(UUID.fromString(updateSubscription.id)!!).get()
        player.subscription = updateSubscription.date
        return ResponseEntity.ok().body(repositoryPlayer.save(player))
    }

}