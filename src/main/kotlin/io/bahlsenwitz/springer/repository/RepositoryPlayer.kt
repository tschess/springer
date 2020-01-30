package io.bahlsenwitz.springer.repository

import io.bahlsenwitz.springer.model.player.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RepositoryPlayer: JpaRepository<Player, UUID> {

    fun findByUsername(username: String): Player?

    fun findByDevice(device: String): Player? //this has to be unique...

}