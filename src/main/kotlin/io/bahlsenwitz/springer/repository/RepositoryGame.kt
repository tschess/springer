package io.bahlsenwitz.springer.repository

import io.bahlsenwitz.springer.model.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RepositoryGame: JpaRepository<Game, UUID> {

}