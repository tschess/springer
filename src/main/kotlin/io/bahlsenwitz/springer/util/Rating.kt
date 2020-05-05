package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.common.Elo
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer

class Rating(private val repositoryPlayer: RepositoryPlayer) {

    private val dateTime: DateTime = DateTime()

    fun update(playerSelf: Player, result: RESULT) {
        val elo00: Int = playerSelf.elo
        val elo01: Elo = Elo(elo00)
        val elo02: Int = elo01.update(result, elo00)
        playerSelf.elo = elo02
        repositoryPlayer.save(playerSelf)
        recalc()
    }

    fun draw(playerSelf: Player, playerOther: Player, game: Game, white: Boolean) {
        val elo00_self: Int = playerSelf.elo
        val elo00_other: Int = playerOther.elo

        val elo01_self: Elo = Elo(elo00_self)
        val elo01_other: Elo = Elo(elo00_other)

        val elo02_self: Int = elo01_self.update(RESULT.DRAW, elo00_other)
        val elo02_other: Int = elo01_other.update(RESULT.DRAW, elo00_self)

        playerSelf.elo = elo02_self
        playerOther.elo = elo02_other

        val disp: Array<Int> = recalc()
        game.white_disp = disp[0]
        game.black_disp = disp[1]
        if(!white){
            game.white_disp = disp[1]
            game.black_disp = disp[0]
        }
        repositoryPlayer.saveAll(listOf(playerSelf, playerOther))
    }

    private fun recalc(playerSelf: Player? = null, playerOther: Player? = null): Array<Int> {
        val list: Array<Int> = arrayOf(0,0)
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
            if(playerSelf != null){
                if(playerSelf.id == player.id){
                    list[0] = disp
                }
            }
            if(playerOther != null){
                if(playerOther.id == player.id){
                    list[1] = disp
                }
            }
        }
        return list
    }
}