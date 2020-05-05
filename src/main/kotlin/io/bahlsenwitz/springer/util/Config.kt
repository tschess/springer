package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.player.Player

class Config {

    fun defaultConfig0(): List<List<String>> {
        val r1: List<String> = arrayListOf("Pawn","Poison","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop")
        val r0: List<String> = arrayListOf("King","","Hunter","Knight","Knight","Knight","Knight","")
        return arrayListOf(r0, r1)
    }
    fun defaultConfig1(): List<List<String>> {
        val r1: List<String> = arrayListOf("Knight","Knight","Knight","Knight","Knight","Knight","Knight","")
        val r0: List<String> = arrayListOf("King","Knight","Knight","Knight","Knight","Knight","Knight","")
        return arrayListOf(r0, r1)
    }
    fun defaultConfig2(): List<List<String>> {
        val r1: List<String> = arrayListOf("","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop")
        val r0: List<String> = arrayListOf("","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop","King")
        return arrayListOf(r0, r1)
    }

    fun get(index: Int, player: Player): List<List<String>> {
        if (index == 0) {
            return player.config0
        }
        if (index == 1) {
            return player.config1
        }
        if (index == 2) {
            return player.config2
        }
        val r1: List<String> = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
        val r0: List<String> = arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
        return arrayListOf(r0, r1)
    }

    fun orient(row00: List<String>, color: String): List<String> {
        val row01: MutableList<String> = mutableListOf()
        for (element: String in row00) {
            if (element == "") {
                row01.add(element)
                continue
            }
            row01.add("${element}${color}_x")
        }
        return row01
    }
}

