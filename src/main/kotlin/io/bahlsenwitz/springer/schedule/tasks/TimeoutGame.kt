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
import io.bahlsenwitz.springer.util.DateTime
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimeoutGame(val repositoryPlayer: RepositoryPlayer, val repositoryGame: RepositoryGame) {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    private val brooklyn = ZoneId.of("America/New_York")

    fun execute() {
        val gameList: List<Game> = repositoryGame.findAll().filter { it.status == STATUS.ONGOING }
        for (game: Game in gameList) {

            val u01: ZonedDateTime = LocalDateTime.now().atZone(this.brooklyn)
            val u02: ZonedDateTime = LocalDateTime.parse(game.updated, this.formatter).atZone(this.brooklyn)
            val durationE1: Duration = Duration.between(u02, u01)
            val period24: Long = 60 * 60 * 24 * 1000.toLong()
            val periodXX: Long = period24 - durationE1.toMillis()
            if (periodXX > 0) {
                return
            }
            game.status = STATUS.RESOLVED
            game.condition = CONDITION.TIMEOUT

            val playerW: Player
            val playerL: Player

            if (game.turn == CONTESTANT.WHITE) {
                game.winner = CONTESTANT.BLACK
                playerW = game.black
                playerL = game.white
            } else {
                game.winner = CONTESTANT.WHITE
                playerW = game.white
                playerL = game.black
            }

            val elo_W_00: Int = playerW.elo
            val elo_W_01: Elo = Elo(elo_W_00)

            val elo_L_00: Int = playerL.elo
            val elo_L_01: Elo = Elo(elo_L_00)

            val elo_W_02: Int = elo_W_01.update(resultActual = RESULT.WIN, eloOpponent = elo_L_00)
            val elo_L_02: Int = elo_L_01.update(resultActual = RESULT.LOSS, eloOpponent = elo_W_00)

            playerW.elo = elo_W_02
            playerL.elo = elo_L_02

            repositoryPlayer.save(playerW)
            repositoryPlayer.save(playerL)
            repositoryGame.save(game)

            /**
             * LEADERBOARD RECALC
             */
            val playerFindAllList: List<Player> = repositoryPlayer.findAll().sorted()
            playerFindAllList.forEachIndexed forEach@{ index, player ->
                if (player.rank == index + 1) {
                    return@forEach
                }
                val rank: Int = (index + 1)
                val disp: Int = player.rank - (index + 1)
                val date: String = DateTime().getDate()
                player.rank = rank
                player.disp = disp
                player.date = date
                repositoryPlayer.save(player)
            }
            //^^^
            game.white_disp = repositoryPlayer.findById(game.white.id).get().disp
            game.black_disp = repositoryPlayer.findById(game.black.id).get().disp
            game.highlight = "9999"
            game.updated = DateTime().getDate()
            repositoryGame.save(game)
        }
    }
}