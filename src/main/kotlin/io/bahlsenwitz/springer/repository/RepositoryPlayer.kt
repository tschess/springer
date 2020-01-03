package io.bahlsenwitz.springer.repository

import io.bahlsenwitz.springer.model.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RepositoryPlayer: JpaRepository<Player,UUID> {

    fun getById(id: UUID): Player

    fun getByName(name: String): Player?

    fun getByDevice(device: String): Player?

}