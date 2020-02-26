package io.bahlsenwitz.springer.schedule.tasks

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
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

class TimeoutGame(val repositoryPlayer: RepositoryPlayer, val repositoryGame: RepositoryGame) {
    private var brooklyn: ZoneId = ZoneId.of("America/New_York")

    fun execute() {
        val gameList: List<Game> = repositoryGame.findAll().filter { it.status == STATUS.ONGOING }
        for (game: Game in gameList) {
            val dateNow: ZonedDateTime = ZonedDateTime.now(brooklyn)
            val dateThen: ZonedDateTime = game.updated.toInstant().atZone(brooklyn)

            val elapsed: Long = Duration.between(dateNow, dateThen).seconds
            if (elapsed.absoluteValue > TimeUnit.HOURS.toSeconds(24)) {
            //if (elapsed.absoluteValue > TimeUnit.MINUTES.toSeconds(1)) {
                game.status = STATUS.RESOLVED
                game.outcome = OUTCOME.TIMEOUT

                val winner: Player = getSetWinner(game)
                val loser: Player = getLoser(game)
                setElo(winner, loser)
            }
        }
    }

    private fun setElo(winner: Player, loser: Player) {
        val wnElo0: Int = winner.elo
        val wnElo: Elo = Elo(wnElo0)

        val lsElo0: Int = loser.elo
        val lsElo: Elo = Elo(lsElo0)

        val wnElo1: Int = wnElo.update(resultActual = RESULT.WIN, eloOpponent = lsElo0)
        val lsElo1: Int = lsElo.update(resultActual = RESULT.LOSS, eloOpponent = wnElo0)

        winner.elo = wnElo1
        loser.elo = lsElo1

        repositoryPlayer.save(winner)
        repositoryPlayer.save(loser)
    }

    private fun getSetWinner(game: Game): Player {
        if (game.turn == CONTESTANT.BLACK) {
            game.winner = CONTESTANT.WHITE
            repositoryGame.save(game)
            return game.white
        }
        game.winner = CONTESTANT.BLACK
        repositoryGame.save(game)
        return game.black
    }

    private fun getLoser(game: Game): Player {
        if (game.turn == CONTESTANT.WHITE) {
            return game.white
        }
        return game.black
    }
}