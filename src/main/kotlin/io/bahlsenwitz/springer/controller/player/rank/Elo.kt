package io.bahlsenwitz.springer.controller.player.rank

import kotlin.math.pow

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