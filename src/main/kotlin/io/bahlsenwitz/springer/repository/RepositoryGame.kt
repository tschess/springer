package io.bahlsenwitz.springer.repository

import io.bahlsenwitz.springer.model.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RepositoryGame: JpaRepository<Game, UUID> {

    @Query(value = "" +
            "SELECT * " +
            "FROM game " +
            "WHERE " +
            "(white = ?1 OR black = ?1)", nativeQuery = true)
    fun getPlayerList(id: UUID): List<Game>

}