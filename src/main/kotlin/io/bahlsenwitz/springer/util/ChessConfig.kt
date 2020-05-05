package io.bahlsenwitz.springer.util

class ChessConfig {

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

    fun getConfigChess(): List<List<String>> {
        val r0: List<String> = arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
        val r1: List<String> = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
        return arrayListOf(r0, r1)
    }
}

