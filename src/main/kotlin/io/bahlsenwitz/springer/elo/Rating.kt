package io.bahlsenwitz.springer.elo

import kotlin.math.pow

class Rating(private var rating: Int) {

    fun update(gameOutcome: Outcome, opponentRating: Int): Int {
        val exponent = (opponentRating - rating).toDouble() / 400.0
        val expectedOutcome = 1.0 / (1.0 + 10.0.pow(exponent))
        val difference = 40 * (gameOutcome.value - expectedOutcome)
        rating += difference.toInt()
        return rating
    }
}

enum class Outcome(val value: Double) {
    Win(1.0),
    Draw(0.5),
    Loss(0.0)
}