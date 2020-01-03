package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.elo.Outcome
import io.bahlsenwitz.springer.elo.Rating
import io.bahlsenwitz.springer.model.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlin.math.absoluteValue

class TimeoutTasks(
    private val repositoryPlayer: RepositoryPlayer,
    private val repositoryGame: RepositoryGame
) {

    fun start() {
        Timer().schedule(0, TimeUnit.MINUTES.toMillis(2)) {
            timeoutTask(threshold = TimeUnit.MINUTES.toSeconds(5), gameList = repositoryGame.getOngoingClock5())
        }
        Timer().schedule(0, TimeUnit.MINUTES.toMillis(30)) {
            timeoutTask(threshold = TimeUnit.HOURS.toSeconds(1), gameList = repositoryGame.getOngoingClock1())
        }
        Timer().schedule(0, TimeUnit.HOURS.toMillis(24)) {
            timeoutTask(threshold = TimeUnit.HOURS.toSeconds(24), gameList = repositoryGame.getOngoingClock24())
            timeoutInvitationsTask()
        }
    }

    private fun timeoutTask(threshold: Long, gameList: List<Game>) {
        val brooklyn = ZoneId.of("America/New_York")

        var elapsedTime: Long
        gameList.forEach {
            if (it.turn == it.white.name) { //white to move...
                elapsedTime = if (it.white_update == "TBD") { //white has yet to move...

                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss.SSSS")
                    val dateTimeFormatted = LocalDateTime.parse(it.created, formatter)
                    val dateTimeZoned = ZonedDateTime.of(dateTimeFormatted, brooklyn)
                    val dateTimeNow = ZonedDateTime.now(brooklyn)
                    Duration.between(dateTimeNow, dateTimeZoned).seconds

                } else {

                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss.SSSS")
                    val dateTimeFormatted = LocalDateTime.parse(it.white_update, formatter)
                    val dateTimeZoned = ZonedDateTime.of(dateTimeFormatted, brooklyn)
                    val dateTimeNow = ZonedDateTime.now(brooklyn)
                    Duration.between(dateTimeNow, dateTimeZoned).seconds

                }

                if (elapsedTime.absoluteValue > threshold) {

                    timeoutGameStats(game = it, winner = it.black.name, catalyst = "TIMEOUT")
                    timeoutPlayerStats(
                        winnerName = it.black.name,
                        winTschx = 2,
                        loserName = it.white.name,
                        loseTschx = 0
                    )
                }
            }
            if (it.turn == it.black.name) { //black to move...

                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss.SSSS")
                val dateTimeFormatted = LocalDateTime.parse(it.black_update, formatter)
                val dateTimeZoned = ZonedDateTime.of(dateTimeFormatted, brooklyn)
                val dateTimeNow = ZonedDateTime.now(brooklyn)
                elapsedTime = Duration.between(dateTimeNow, dateTimeZoned).seconds

                if (elapsedTime.absoluteValue > threshold) {
                    timeoutGameStats(game = it, winner = it.white.name, catalyst = "TIMEOUT")
                    timeoutPlayerStats(
                        winnerName = it.white.name,
                        winTschx = 2,
                        loserName = it.black.name,
                        loseTschx = 0
                    )
                }
            }
        }
    }

    private fun timeoutInvitationsTask() {
        val brooklyn = ZoneId.of("America/New_York")

        val pendingInvitationList = repositoryGame.getProposed()
        pendingInvitationList.forEach {
            if (it.created != "TBD") {

                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss.SSSS")
                val dateTimeFormatted = LocalDateTime.parse(it.created, formatter)
                val dateTimeZoned = ZonedDateTime.of(dateTimeFormatted, brooklyn)
                val dateTimeNow = ZonedDateTime.now(brooklyn)
                val elapsedTime = Duration.between(dateTimeNow, dateTimeZoned).seconds

                if (elapsedTime.absoluteValue > TimeUnit.DAYS.toSeconds(1)) {
                    timeoutGameStats(game = it, winner = it.black.name, catalyst = "INVITATION")
                    timeoutPlayerStats(
                        winnerName = it.black.name,
                        winTschx = 1,
                        loserName = it.white.name,
                        loseTschx = 0
                    )
                }
            }
        }
    }

    private fun timeoutGameStats(game: Game, winner: String, catalyst: String) {
        game.winner = winner
        game.status = "RESOLVED"
        game.catalyst = catalyst

        val zoneId = ZoneId.of("America/New_York")
        val current = ZonedDateTime.now(zoneId)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss.SSSS")
        val formatted = current.format(formatter)

        game.updated = formatted

        repositoryGame.save(game)
    }

    private fun timeoutPlayerStats(winnerName: String, winTschx: Int, loserName: String, loseTschx: Int) {
        val winner = repositoryPlayer.getByName(winnerName)!!
        val loser = repositoryPlayer.getByName(loserName)!!

        val ratingWinner = Rating(rating = winner.elo)
        val eloWinner = ratingWinner.update(gameOutcome = Outcome.Win, opponentRating = loser.elo)
        winner.elo = eloWinner
        winner.tschx += winTschx

        val ratingLoser = Rating(rating = loser.elo)
        val eloLoser = ratingLoser.update(gameOutcome = Outcome.Loss, opponentRating = winner.elo)
        loser.elo = eloLoser
        winner.tschx += loseTschx

        repositoryPlayer.save(winner)
        repositoryPlayer.save(loser)
    }
}