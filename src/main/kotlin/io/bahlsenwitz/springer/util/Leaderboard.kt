package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer

class Leaderboard(private val repositoryPlayer: RepositoryPlayer) {

    private val dateTime: DateTime = DateTime()

    fun recalc() {
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