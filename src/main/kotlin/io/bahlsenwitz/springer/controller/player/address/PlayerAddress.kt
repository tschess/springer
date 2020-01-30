package io.bahlsenwitz.springer.controller.player.address

import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class PlayerAddress(private val repositoryPlayer: RepositoryPlayer) {

    fun address(updateAddress: UpdateAddress): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(updateAddress.id)!!
        val player = repositoryPlayer.getById(uuid)
        //val address: String = updateAddress.address
        //player.address = address
        //val name: String = updateAddress.id
        //player.name = name
        //val surname: String = updateAddress.surname
        //player.surname = surname
        //val email: String = updateAddress.email
        //player.email = email
        //val updated: String = updateAddress.updated
        //player.updated = updated
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    data class UpdateAddress(
        val id: String,
        val address: String,
        val name: String,
        val surname: String,
        val email: String,
        val updated: String
    )

}