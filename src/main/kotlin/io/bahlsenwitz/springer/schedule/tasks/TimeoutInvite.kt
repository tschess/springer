package io.bahlsenwitz.springer.schedule.tasks

import io.bahlsenwitz.springer.model.common.Elo
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimeoutInvite(val repositoryPlayer: RepositoryPlayer, val repositoryGame: RepositoryGame) {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    private val brooklyn = ZoneId.of("America/New_York")

    fun execute() {
        val inviteList: List<Game> = repositoryGame.findAll().filter { it.status == STATUS.PROPOSED }
        for (invite: Game in inviteList) {

            val u01: ZonedDateTime = LocalDateTime.now().atZone(this.brooklyn)
            val u02: ZonedDateTime = LocalDateTime.parse(invite.updated, this.formatter).atZone(this.brooklyn)
            val durationE1: Duration = Duration.between(u02, u01)
            val period24: Long = 60 * 60 * 24 * 1000.toLong()
            val periodXX: Long = period24 - durationE1.toMillis()
            if (periodXX <= 0) {
                invite.status = STATUS.RESOLVED
                invite.condition = CONDITION.EXPIRED
                repositoryGame.save(invite)

                //take points away from the one who didn't respond...
                val penalty: Player = getPenalty(invite)
                val elo0: Int = penalty.elo
                val elo1: Elo = Elo(elo0)
                val eloX: Int = elo1.update(resultActual = RESULT.LOSS, eloOpponent = elo0)
                penalty.elo = eloX
                repositoryPlayer.save(penalty)
            }
        }
    }

    private fun getPenalty(game: Game): Player {
        if (game.turn == CONTESTANT.WHITE) {
            return game.black
        }
        return game.white
    }
}


