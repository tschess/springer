package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.common.Elo
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.OUTCOME
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class GameMate(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    private val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun mate(id_game: String): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(id_game)!!
        val game: Game = repositoryGame.findById(uuid).get()

        game.status = STATUS.RESOLVED
        game.outcome = OUTCOME.CHECKMATE
        /***/
        if (game.turn == CONTESTANT.WHITE) {
            game.winner = CONTESTANT.BLACK

            val wnElo0: Int = game.black.elo
            val wnElo: Elo = Elo(wnElo0)

            val lsElo0: Int = game.white.elo
            val lsElo: Elo = Elo(lsElo0)

            val wnElo1: Int = wnElo.update(resultActual = RESULT.WIN, eloOpponent = lsElo0)
            val lsElo1: Int = lsElo.update(resultActual = RESULT.LOSS, eloOpponent = wnElo0)

            game.black.elo = wnElo1
            game.white.elo = lsElo1
        } else {
            game.winner = CONTESTANT.WHITE

            val wnElo0: Int = game.white.elo
            val wnElo: Elo = Elo(wnElo0)

            val lsElo0: Int = game.black.elo
            val lsElo: Elo = Elo(lsElo0)

            val wnElo1: Int = wnElo.update(resultActual = RESULT.WIN, eloOpponent = lsElo0)
            val lsElo1: Int = lsElo.update(resultActual = RESULT.LOSS, eloOpponent = wnElo0)

            game.black.elo = lsElo1
            game.white.elo = wnElo1
        }
        repositoryPlayer.save(game.black)
        repositoryPlayer.save(game.white)

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
            val date: Date = Date.from(ZonedDateTime.now(ZoneId.of("America/New_York")).toInstant())
            player.date = date
            val rank: Int = (index + 1)
            player.rank = rank
            repositoryPlayer.save(player)
        }
        //^^^

        game.white_disp = repositoryPlayer.findById(game.white.id).get().disp
        game.black_disp = repositoryPlayer.findById(game.black.id).get().disp
        game.highlight = "TBD"
        game.updated = DATE_TIME_GENERATOR.rightNowString()
        repositoryGame.save(game)
        return ResponseEntity.ok("{\"success\": \"ok\"}")
    }
}