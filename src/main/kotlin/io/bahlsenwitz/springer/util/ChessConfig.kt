package io.bahlsenwitz.springer.util

class ChessConfig {

    fun getConfigChess(): List<List<String>> {
        val r0: List<String> = arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
        val r1: List<String> = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
        return arrayListOf(r0, r1)
    }
}

