package io.bahlsenwitz.springer.schedule.tasks

import io.bahlsenwitz.springer.controller.game.menu.actual.invite.GameAck
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
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

class TimeoutGame(val repositoryPlayer: RepositoryPlayer, val repositoryGame: RepositoryGame) {
    private var brooklyn: ZoneId = ZoneId.of("America/New_York")
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    fun execute() {
        val gameList: List<Game> = repositoryGame.findAll().filter { it.status == STATUS.ONGOING }
        for (game: Game in gameList) {
            val dateNow: ZonedDateTime = ZonedDateTime.now(brooklyn)
            val dateThen: ZonedDateTime = LocalDateTime.parse(game.updated, formatter).atZone(brooklyn)

            val elapsed: Long = Duration.between(dateNow, dateThen).seconds
            //if (elapsed.absoluteValue > TimeUnit.HOURS.toSeconds(24)) {
            if (elapsed.absoluteValue > TimeUnit.MINUTES.toSeconds(1)) {
                game.status = STATUS.RESOLVED
                game.outcome = OUTCOME.TIMEOUT

                val winner: Player = getSetWinner(game)
                val loser: Player = getLoser(game)
                setElo(winner, loser)

                /**
                 * LEADERBOARD RECALC
                 */
                val playerFindAllList: List<Player> = repositoryPlayer.findAll().sorted()
                playerFindAllList.forEachIndexed forEach@{ index, player ->
                    if (player.rank == index + 1) {
                        player.disp = 0
                        repositoryPlayer.save(player)
                        return@forEach
                    }
                    val disp: Int = player.rank - (index + 1)
                    player.disp = disp
                    val date = GameAck.FORMATTER.format(ZonedDateTime.now(Game.BROOKLYN)).toString()
                    player.date = date
                    val rank: Int = (index + 1)
                    player.rank = rank
                    repositoryPlayer.save(player)
                }
                //^^^

                game.white_disp = repositoryPlayer.findById(game.white.id).get().disp
                game.black_disp = repositoryPlayer.findById(game.black.id).get().disp
                game.highlight = "TBD"
                game.updated = GameAck.FORMATTER.format(ZonedDateTime.now(Game.BROOKLYN)).toString()
                repositoryGame.save(game)
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