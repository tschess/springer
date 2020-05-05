package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.common.Elo
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer

class Rating(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer) {

    private val dateTime: DateTime = DateTime()

    fun update(playerSelf: Player, result: RESULT) {
        val elo00: Int = playerSelf.elo
        val elo01: Elo = Elo(elo00)
        val elo02: Int = elo01.update(result, elo00)
        playerSelf.elo = elo02
        repositoryPlayer.save(playerSelf)
        recalc()
    }

    fun mate(game: Game) {
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
        repositoryPlayer.saveAll(listOf(game.white, game.black))
        val disp: Array<Int> = recalc(game.white, game.black)
        game.white_disp = disp[0]
        game.black_disp = disp[1]
        repositoryGame!!.save(game)
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

        repositoryPlayer.saveAll(listOf(game.white, game.black))
        val disp: Array<Int> = recalc(game.white, game.black)
        game.white_disp = disp[0]
        game.black_disp = disp[1]
        repositoryGame!!.save(game)
    }

    private fun recalc(player00: Player? = null, player01: Player? = null): Array<Int> {
        val list: Array<Int> = arrayOf(0, 0)
        val leaderboard: List<Player> = repositoryPlayer.findAll().sorted()
        for ((index: Int, player: Player) in leaderboard.withIndex()) {
            val rank00: Int = player.rank
            val rank01: Int = index + 1
            if (rank00 == rank01) {
                continue
            }
            val disp: Int = rank00 - rank01
            player.disp = disp
            player.date = dateTime.getDate()
            repositoryPlayer.save(player)
            if (player00 != null) {
                if (player00.id == player.id) {
                    list[0] = disp
                }
            }
            if (player01 != null) {
                if (player01.id == player.id) {
                    list[1] = disp
                }
            }
        }
        return list
    }
}