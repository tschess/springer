package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.player.Player
import java.util.ArrayList

class Config {

    val row: List<String> = arrayListOf("", "", "", "", "", "", "", "")

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

    fun defaultConfig3(): List<List<String>> {
        val r1: List<String> = arrayListOf("","","","Queen","Knight","Queen","","")
        val r0: List<String> = arrayListOf("","","","Queen","King","Queen","","")
        return arrayListOf(r0, r1)
    }

    fun defaultConfig4(): List<List<String>> {
        val r1: List<String> = arrayListOf("","Bishop","Knight","Knight","Knight","Knight","Bishop","")
        val r0: List<String> = arrayListOf("","Rook","Bishop","King","Rook","Bishop","Rook","")
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

//    fun generateState(white: List<List<String>>): List<List<String>> {
//        val color: String = "White"
//        val row00: List<String> = orient(white[0], color)
//        val row01: List<String> = orient(white[1], color)
//        val black: List<List<String>> = quickBlack()
//        return arrayListOf(row00, row01, row, row, row, row, black[1], black[0])
//    }
}

