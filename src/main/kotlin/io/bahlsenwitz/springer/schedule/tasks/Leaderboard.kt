package io.bahlsenwitz.springer.schedule.tasks

import io.bahlsenwitz.springer.controller.game.menu.actual.invite.GameAck
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class Leaderboard(val repositoryPlayer: RepositoryPlayer) {

    fun recalc() {
        val playerList: List<Player> = repositoryPlayer.findAll().sorted()
        playerList.forEachIndexed forEach@{ index, player ->
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
    }
}