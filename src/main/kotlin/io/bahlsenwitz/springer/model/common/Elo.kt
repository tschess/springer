package io.bahlsenwitz.springer.model.common

import kotlin.math.pow

class Elo(private var elo: Int) {

    fun update(resultActual: RESULT, eloOpponent: Int): Int {
        val exponent = (eloOpponent - elo).toDouble() / 400.0
        val resultExpected = 1.0 / (1.0 + 10.0.pow(exponent))
        val difference = 40 * (resultActual.value - resultExpected)
        elo += difference.toInt()
        return elo
    }
}

enum class RESULT(val value: Double) {
    ACTION(1.0),
    WIN(1.0),
    DRAW(0.5),
    LOSS(0.0)
}