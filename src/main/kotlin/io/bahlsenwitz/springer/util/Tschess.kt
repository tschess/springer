package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.game.CONTESTANT

class Tschess {

    fun setTurn(turn: CONTESTANT): CONTESTANT {
        if (turn == CONTESTANT.WHITE) {
            return CONTESTANT.BLACK
        }
        return CONTESTANT.WHITE
    }

}