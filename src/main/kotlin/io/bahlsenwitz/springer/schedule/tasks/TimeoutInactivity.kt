package io.bahlsenwitz.springer.schedule.tasks

import io.bahlsenwitz.springer.model.rating.RESULT
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import io.bahlsenwitz.springer.util.Rating
import java.time.Duration
import java.time.ZonedDateTime

class TimeoutInactivity(val repositoryPlayer: RepositoryPlayer) {

    private val dateTime: DateTime = DateTime()
    private val rating: Rating = Rating(repositoryPlayer = repositoryPlayer)

    fun execute() {
        val listPlayer: List<Player> = repositoryPlayer.findAll()
        for (player: Player in listPlayer) {
            val time00: ZonedDateTime = dateTime.rn()
            val time01: ZonedDateTime = dateTime.getDate(player.updated)
            val duration: Duration = Duration.between(time01, time00)
            val period24: Long = 60 * 60 * 24 * 1000.toLong()
            val periodXX: Long = period24 - duration.toMillis()
            if (periodXX > 0) {
                continue
            }
            rating.update(player, RESULT.LOSS)
        }
    }
}