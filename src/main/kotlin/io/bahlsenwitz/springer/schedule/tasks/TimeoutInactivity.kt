package io.bahlsenwitz.springer.schedule.tasks

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.model.rating.Elo
import io.bahlsenwitz.springer.model.rating.RESULT
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime
import java.time.Duration
import java.time.ZonedDateTime

class TimeoutInactivity(val repositoryPlayer: RepositoryPlayer) {

    private val dateTime: DateTime = DateTime()

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
            val elo00: Int = player.elo
            val elo01: Elo = Elo(elo00)
            val elo02: Int = elo01.update(RESULT.LOSS, elo00)
            player.elo = elo02
            repositoryPlayer.save(player)
        }
    }
}