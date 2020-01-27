package io.bahlsenwitz.springer.repository

import io.bahlsenwitz.springer.model.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import org.springframework.data.domain.Sort


@Repository
interface RepositoryPlayer : JpaRepository<Player, UUID> {

    fun getById(id: UUID): Player

    fun getByUsername(username: String): Player?

    fun getByDevice(device: String): Player?

    override fun findAll(): List<Player> {
        return findAll(sortByEloDesc())
    }

    private fun sortByEloDesc(): Sort {
        return Sort(Sort.Direction.DESC, "elo")
    }

}