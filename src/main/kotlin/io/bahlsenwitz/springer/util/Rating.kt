package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.rating.Elo
import io.bahlsenwitz.springer.model.rating.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.time.ZonedDateTime

class Rating(
    private val repositoryGame: RepositoryGame? = null,
    private val repositoryPlayer: RepositoryPlayer? = null) {

    private val dateTime: DateTime = DateTime()


    fun activate(player: Player) {
        val eloUpdate: Int = player.elo + 1
        player.elo = eloUpdate
        repositoryPlayer!!.save(player)
        recalc()
    }

    fun update(playerSelf: Player, result: RESULT) {
        val elo00: Int = playerSelf.elo
        val elo01: Elo = Elo(elo00)
        val elo02: Int = elo01.update(result, elo00)
        playerSelf.elo = elo02
        repositoryPlayer!!.save(playerSelf)
        recalc()
    }

    fun resolve(game: Game) {
        if (game.turn == CONTESTANT.WHITE) {
            game.winner = CONTESTANT.BLACK

            val elo00_win: Int = game.black.elo
            val elo00_lst: Int = game.white.elo

            val elo01_win: Elo = Elo(elo00_win)
            val elo01_lst: Elo = Elo(elo00_lst)

            val elo02_win: Int = elo01_win.update(RESULT.WIN, elo00_lst)
            val elo02_lst: Int = elo01_lst.update(RESULT.LOSS, elo00_win)

            game.white.elo = elo02_lst
            game.black.elo = elo02_win
        } else {
            game.winner = CONTESTANT.WHITE
            val elo00_win: Int = game.white.elo
            val elo00_lst: Int = game.black.elo

            val elo01_win: Elo = Elo(elo00_win)
            val elo01_lst: Elo = Elo(elo00_lst)

            val elo02_win: Int = elo01_win.update(RESULT.WIN, elo00_lst)
            val elo02_lst: Int = elo01_lst.update(RESULT.LOSS, elo00_win)

            game.white.elo = elo02_win
            game.black.elo = elo02_lst
        }
        repositoryPlayer!!.saveAll(listOf(game.white, game.black))
        repositoryGame!!.save(game)
        recalc()
    }

    fun expire(invite: Game) {
        if (invite.challenger == CONTESTANT.WHITE) {
            val elo00_win: Int = invite.white.elo
            val elo00_lst: Int = invite.black.elo

            val elo01_win: Elo = Elo(elo00_win)
            val elo01_lst: Elo = Elo(elo00_lst)

            val elo02_win: Int = elo01_win.update(RESULT.WIN, elo00_lst)
            val elo02_lst: Int = elo01_lst.update(RESULT.LOSS, elo00_win)

            invite.white.elo = elo02_win
            invite.black.elo = elo02_lst
        } else {
            val elo00_win: Int = invite.black.elo
            val elo00_lst: Int = invite.white.elo

            val elo01_win: Elo = Elo(elo00_win)
            val elo01_lst: Elo = Elo(elo00_lst)

            val elo02_win: Int = elo01_win.update(RESULT.WIN, elo00_lst)
            val elo02_lst: Int = elo01_lst.update(RESULT.LOSS, elo00_win)

            invite.black.elo = elo02_win
            invite.white.elo = elo02_lst
        }
        repositoryPlayer!!.saveAll(listOf(invite.white, invite.black))
        repositoryGame!!.save(invite)
        recalc()
    }

    fun draw(game: Game) {
        val elo00_white: Int = game.white.elo
        val elo00_black: Int = game.black.elo

        val elo01_white: Elo = Elo(elo00_white)
        val elo01_black: Elo = Elo(elo00_black)

        val elo02_white: Int = elo01_white.update(RESULT.DRAW, elo00_black)
        val elo02_black: Int = elo01_black.update(RESULT.DRAW, elo00_white)

        game.white.elo = elo02_white
        game.black.elo = elo02_black

        repositoryPlayer!!.saveAll(listOf(game.white, game.black))
        repositoryGame!!.save(game)
        recalc()
    }

    fun addition(player: Player): Player {
        val leaderboard: List<Player> = repositoryPlayer!!
            .findAll()
            .filter {
                getActive(it)
            }
            .sorted()
        val tlo: Int = leaderboard.last().elo - 1
        player.elo = tlo
        player.rank = leaderboard.count() + 1
        return repositoryPlayer.save(player)
    }

    private fun getActive(player: Player): Boolean {
        val time00: ZonedDateTime = dateTime.rn().minusDays(7L)
        val time01: ZonedDateTime = dateTime.getDate(player.updated)
        if (time00.isBefore(time01)) {
            return true
        }
        return false
    }

    private fun recalc() {
        val leaderboard: List<Player> = repositoryPlayer!!
            .findAll()
            .filter {
                getActive(it)
            }
            .sorted()
        for ((index: Int, player: Player) in leaderboard.withIndex()) {
            val rank00: Int = player.rank
            val rankXX: Int = index + 1
            if (rank00 == rankXX) {
               continue
            }
            player.rank = rankXX
            val disp: Int = rank00 - rankXX
            player.disp = disp
            player.date = dateTime.getDate()
            repositoryPlayer.save(player)
        }
    }

}