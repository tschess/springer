package io.bahlsenwitz.springer.controller.game.util

import java.util.*

class FormatPersist {

    fun poisonReveal(state: List<List<String>>): List<List<String>> {
        val r0 = ArrayList<List<String>>()
        for (row: List<String> in state) {
            val r1 = ArrayList<String>()
            for (value: String in row) {
                when {
                    value.contains("PoisonWhite") -> r1.add("RevealWhite")
                    value.contains("PoisonBlack") -> r1.add("RevealBlack")
                    else -> r1.add(value)
                }
            }
            r0.add(r1)
        }
        return r0
    }
}