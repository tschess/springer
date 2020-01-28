package io.bahlsenwitz.springer.controller.player.rank

import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import kotlin.math.pow

class PlayerRank(private val repositoryPlayer: RepositoryPlayer) {

    fun order() {

        val playerAll: List<Player> = repositoryPlayer.findAll().sorted()



    }

}

class Elo(private var elo: Int) {

    fun update(outcomeActual: Outcome, eloOpponent: Int): Int {
        val exponent = (eloOpponent - elo).toDouble() / 400.0
        val outcomeExpected = 1.0 / (1.0 + 10.0.pow(exponent))
        val difference = 40 * (outcomeActual.value - outcomeExpected)
        elo += difference.toInt()
        return elo
    }
}

enum class Outcome(val value: Double) {
    Win(1.0),
    Draw(0.5),
    Loss(0.0)
}