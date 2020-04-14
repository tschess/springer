package io.bahlsenwitz.springer.schedule

import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.schedule.tasks.BackUp
import io.bahlsenwitz.springer.schedule.tasks.TimeoutGame
import io.bahlsenwitz.springer.schedule.tasks.TimeoutInvite
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class Schedule(val repositoryPlayer: RepositoryPlayer, val repositoryGame: RepositoryGame) {

    fun execute() {
        val meridiem: Long = TimeUnit.HOURS.toMillis(12)
        Timer().schedule(0, meridiem) {
            TimeoutInvite(repositoryPlayer, repositoryGame).execute()
            TimeoutGame(repositoryPlayer, repositoryGame).execute()
        }
        val daily: Long = TimeUnit.HOURS.toMillis(24)
        Timer().schedule(0, daily) {
            BackUp(repositoryPlayer, repositoryGame).execute()
        }
    }
}