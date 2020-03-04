package io.bahlsenwitz.springer.generator.common

class Default {
    companion object {
        fun state(): List<List<String>> {
            val row0: List<String> =
                arrayListOf(
                    "RookWhite_x",
                    "KnightWhite_x",
                    "BishopWhite_x",
                    "QueenWhite_x",
                    "KingWhite_x",
                    "BishopWhite_x",
                    "KnightWhite_x",
                    "RookWhite_x"
                )
            val row1: List<String> = arrayListOf(
                "PawnWhite_x",
                "PawnWhite_x",
                "PawnWhite_x",
                "PawnWhite_x",
                "PawnWhite_x",
                "PawnWhite_x",
                "PawnWhite_x",
                "PawnWhite_x"
            )
            val row2: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row3: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row4: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row5: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row6: List<String> = arrayListOf(
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x",
                "PawnBlack_x"
            )
            val row7: List<String> =
                arrayListOf(
                    "RookBlack_x",
                    "KnightBlack_x",
                    "BishopBlack_x",
                    "QueenBlack_x",
                    "KingBlack_x",
                    "BishopBlack_x",
                    "KnightBlack_x",
                    "RookBlack_x"
                )
            return arrayListOf(row0, row1, row2, row3, row4, row5, row6, row7)
        }
    }
}
