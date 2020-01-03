package io.bahlsenwitz.springer.repository

import io.bahlsenwitz.springer.model.Iapetus
import io.bahlsenwitz.springer.model.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RepositoryIapetus: JpaRepository<Iapetus, UUID> {

    @Query(value = "SELECT COUNT(*) FROM iapetus WHERE owner IS NULL", nativeQuery = true)
    fun getRemaining(): Int

    @Query(value = "SELECT * FROM iapetus WHERE owner IS NULL LIMIT 1", nativeQuery = true)
    fun getNext(): Iapetus

    @Query(value = "SELECT * FROM iapetus WHERE owner = :owner", nativeQuery = true)
    fun getByOwnerId(@Param("owner") owner: Player): Iapetus?

}
