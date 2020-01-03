package io.bahlsenwitz.springer.repository

import io.bahlsenwitz.springer.model.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RepositoryGame: JpaRepository<Game,UUID> {

    @Query(value = "" +
            "SELECT * " +
            "FROM game " +
            "WHERE " +
            "(white = ?1 AND black = ?2) " +
            "OR " +
            "(white = ?2 AND black = ?1)", nativeQuery = true)
    fun getGameListMutual(player0: UUID, player1: UUID): List<Game>

    @Query(value = "" +
            "SELECT * " +
            "FROM game " +
            "WHERE " +
            "(white = ?1 OR black = ?1)", nativeQuery = true)
    fun getGameListPlayer(id: UUID): List<Game>

    @Query(value = "SELECT * FROM game WHERE status = 'ONGOING' AND clock = '5'", nativeQuery = true)
    fun getOngoingClock5(): List<Game>

    @Query(value = "SELECT * FROM game WHERE status = 'ONGOING' AND clock = '1'", nativeQuery = true)
    fun getOngoingClock1(): List<Game>

    @Query(value = "SELECT * FROM game WHERE status = 'ONGOING' AND clock = '24'", nativeQuery = true)
    fun getOngoingClock24(): List<Game>

    @Query(value = "SELECT * FROM game WHERE status = 'PROPOSED'", nativeQuery = true)
    fun getProposed(): List<Game>

}
