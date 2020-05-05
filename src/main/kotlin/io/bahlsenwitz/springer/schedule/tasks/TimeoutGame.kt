package io.bahlsenwitz.springer.schedule.tasks

import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import java.time.Duration
import java.time.ZonedDateTime

class TimeoutGame(val repositoryPlayer: RepositoryPlayer, val repositoryGame: RepositoryGame) {

    private val dateTime: DateTime = DateTime()
    private val rating: Rating = Rating(repositoryGame, repositoryPlayer)

    fun execute() {
        val listOngoing: List<Game> = repositoryGame.findAll().filter { it.status == STATUS.ONGOING }
        for (game: Game in listOngoing) {
            val time00: ZonedDateTime = dateTime.rn()
            val time01: ZonedDateTime = dateTime.getDate(game.updated)
            val duration: Duration = Duration.between(time01, time00)
            val period24: Long = 60 * 60 * 24 * 1000.toLong()
            val periodXX: Long = period24 - duration.toMillis()
            if (periodXX > 0) {
                continue
            }
            game.status = STATUS.RESOLVED
            game.condition = CONDITION.TIMEOUT
            game.updated = dateTime.getDate()
            rating.resolve(game)
        }
    }
}