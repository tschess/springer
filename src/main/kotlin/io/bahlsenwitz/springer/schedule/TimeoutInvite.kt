package io.bahlsenwitz.springer.schedule

import io.bahlsenwitz.springer.model.common.Elo
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.OUTCOME
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlin.math.absoluteValue

class TimeoutInvite(val repositoryPlayer: RepositoryPlayer, val repositoryGame: RepositoryGame) {
    var brooklyn: ZoneId = ZoneId.of("America/New_York")

    fun execute() {
        val daily: Long = TimeUnit.HOURS.toMillis(24)
        val minutely: Long = TimeUnit.MINUTES.toMillis(1)

        Timer().schedule(0, minutely) { // TODO: this will be a day (for now...)
            expire()
        }
    }

    private fun expire() {
        val inviteList: List<Game> = repositoryGame.findAll().filter { it.status == STATUS.PROPOSED }
        for (invite: Game in inviteList) {
            val dateNow: ZonedDateTime = ZonedDateTime.now(brooklyn)
            val dateThen: ZonedDateTime = ZonedDateTime.of(localDateTime(invite.created), brooklyn)

            val elapsed: Long = Duration.between(dateNow, dateThen).seconds
            if (elapsed.absoluteValue > TimeUnit.HOURS.toSeconds(24)) {
            //if (elapsed.absoluteValue > TimeUnit.MINUTES.toSeconds(1)) {

                invite.status = STATUS.RESOLVED
                invite.outcome = OUTCOME.EXPIRED
                repositoryGame.save(invite)

                //take points away from the one who didn't respond...
                val penalty: Player = getPenalty(invite)
                val elo0: Int = penalty.elo
                val elo: Elo = Elo(elo0)
                val elo1: Int = elo.update(resultActual = RESULT.LOSS, eloOpponent = elo0)
                penalty.elo = elo1
                repositoryPlayer.save(penalty)
            }
        }
    }

    private fun getPenalty(game: Game): Player {
        if(game.turn == CONTESTANT.WHITE){
            return game.black
        }
        return game.white
    }

    private fun localDateTime(convert: Date): LocalDateTime {
        return convert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }
}