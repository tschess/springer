package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.common.Elo
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer

class Rating(private val repositoryPlayer: RepositoryPlayer) {

    private val dateTime: DateTime = DateTime()

    fun update(playerSelf: Player, result: RESULT){
        val elo00: Int = playerSelf.elo
        val elo01: Elo = Elo(elo00)
        val elo02: Int = elo01.update(result, elo00)
        playerSelf.elo = elo02
        repositoryPlayer.save(playerSelf)
        recalc()
    }

    private fun recalc() {
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
        }
    }
}