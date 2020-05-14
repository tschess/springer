package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.time.ZonedDateTime
import java.util.*

class Churn(private val repositoryPlayer: RepositoryPlayer, private val repositoryGame: RepositoryGame) {

    private val comparatorAlt: ComparatorAlt = ComparatorAlt(repositoryGame)

    fun calculate(player: Player): ResponseEntity<Any> {

        val topTop: List<Player> = repositoryPlayer.findAll().sorted().take(10)
        val sorted: List<Player> = topTop.sortedWith(comparatorAlt).filter { it.id != player.id }
        val opponent: Player? = sorted.shuffled().take(1)[0]

        var body: MutableMap<String, String> = HashMap()

        body["top_top"] = topTop.size.toString()
        body["sorted"] = sorted.size.toString()
        if(opponent == null){
            body["opponent"] = "FUCK"
        }else {
            body["opponent"] = opponent.id.toString()
        }

        return ResponseEntity.ok().body(body)

        ///return


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