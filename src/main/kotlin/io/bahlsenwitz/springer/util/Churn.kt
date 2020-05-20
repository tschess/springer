package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.time.ZonedDateTime
import java.util.*

class Churn(private val repositoryPlayer: RepositoryPlayer, private val repositoryGame: RepositoryGame? = null) {

    //private val comparatorAlt: ComparatorAlt = ComparatorAlt(repositoryGame)

    fun calculate(player: Player): Player {
        val listHead: List<Player> = repositoryPlayer.findAll().filter { it.id != player.id }.sorted().take(11)
        return listHead.shuffled().take(1)[0]
    }
}

class ComparatorAlt(private val repositoryGame: RepositoryGame) : Comparator<Player> {

    private val dateTime: DateTime = DateTime()

    override fun compare(player00: Player, player01: Player): Int {
        val list00: List<Game> = repositoryGame.findPlayerList(player00.id)
        val ongoing00: Int = list00.filter { it.status == STATUS.ONGOING }.size
        val list01: List<Game> = repositoryGame.findPlayerList(player01.id)
        val ongoing01: Int = list01.filter { it.status == STATUS.ONGOING }.size
        if (ongoing00 == ongoing01) {
            val date00: ZonedDateTime = dateTime.getDate(player00.updated)
            val date01: ZonedDateTime = dateTime.getDate(player01.updated)
            //01 is more recent, so return 00 with higher priority
            if (date00.isBefore(date01)) {
                return -1
            }
            return 1
        }
        return ongoing00.compareTo(ongoing01)
    }

}