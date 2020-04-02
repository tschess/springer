package io.bahlsenwitz.springer.generator.backup

import io.bahlsenwitz.springer.model.game.*
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayList

class GeneratorGame(
    private val repositoryPlayer: RepositoryPlayer,
    private val repositoryGame: RepositoryGame
) {
    private val IDX_ID = 0
    private val IDX_STATE = 1
    private val IDX_STATUS = 2
    private val IDX_OUTCOME = 3
    private val IDX_MOVES = 4
    private val IDX_WHITE_ID = 5
    private val IDX_WHITE_ELO = 6
    private val IDX_WHITE_DISP = 7

    private val IDX_BLACK_ID = 8
    private val IDX_BLACK_ELO = 9
    private val IDX_BLACK_DISP = 10

    private val IDX_CHALLENGER = 11
    private val IDX_WINNER = 12
    private val IDX_TURN = 13
    private val IDX_ON_CHECK = 14
    private val IDX_HIGHLIGHT = 15
    private val IDX_UPDATED = 16


    fun generate(file: File) {
        repositoryGame.deleteAll()
        val gameList = ArrayList<Game>()

        val bufferedReader: BufferedReader = BufferedReader(FileReader(file))
        bufferedReader.use { fileReader ->
            fileReader.readLine()
            var line: String?
            line = fileReader.readLine()
            while (line != null) {
                val tokens: List<String> = line.split(";")
                if (tokens.isNotEmpty()) {
                    val id: UUID = UUID.fromString(tokens[IDX_ID])!! //0

                    var state: List<List<String>>? = null
                    val stateString: String = tokens[IDX_STATE]
                    if (stateString != "NULL") {
                        //val state: List<List<String>> = generateState(stateString = stateString) //1
                        state = generateState(stateString = stateString) //1
                    }
                    val status: STATUS = STATUS.valueOf(tokens[IDX_STATUS]) //2
                    val CONDITION: CONDITION = CONDITION.valueOf(tokens[IDX_OUTCOME]) //3
                    val moves: Int = tokens[IDX_MOVES].toInt() //4
                    val whiteIdString: String = tokens[IDX_WHITE_ID]
                    val white_id: UUID = UUID.fromString(whiteIdString)!!

                    val white: Player = repositoryPlayer.findById(white_id).get() //5
                    val white_elo: Int = tokens[IDX_WHITE_ELO].toInt() //6

                    var white_disp: Int? = null
                    val white_disp_0: String = tokens[IDX_WHITE_DISP] //7
                    if (white_disp_0 != "NULL") {
                        white_disp = white_disp_0.toInt()
                    }

                    val blackIdString: String = tokens[IDX_BLACK_ID]
                    val black_id: UUID = UUID.fromString(blackIdString)!!
                    val black: Player = repositoryPlayer.findById(black_id).get() //9
                    val black_elo: Int = tokens[IDX_BLACK_ELO].toInt() //10

                    var black_disp: Int? = null
                    val black_disp_0: String = tokens[IDX_BLACK_DISP] //11
                    if (black_disp_0 != "NULL") {
                        black_disp = black_disp_0.toInt()
                    }


                    var challenger: CONTESTANT? = null
                    val challenger_0: String = tokens[IDX_CHALLENGER] //11
                    if (challenger_0 != "NULL") {
                        challenger = CONTESTANT.valueOf(challenger_0)
                    }
                    var winner: CONTESTANT? = null
                    val winner_0: String = tokens[IDX_WINNER] //14
                    if (winner_0 != "NULL") {
                        winner = CONTESTANT.valueOf(winner_0)
                    }
                    val turn: CONTESTANT = CONTESTANT.valueOf(tokens[IDX_TURN]) //15
                    val on_check: Boolean = tokens[IDX_ON_CHECK].toBoolean() //16
                    val highlight: String = tokens[IDX_HIGHLIGHT] //17
                    val updated: String = tokens[IDX_UPDATED] //18

                    val game = Game(
                        id = id, //0
                        state = state, //1
                        status = status, //2
                        condition = CONDITION, //3
                        moves = moves, //4
                        white = white, //5
                        white_elo = white_elo, //6
                        white_disp = white_disp, //7

                        black = black, //8
                        black_elo = black_elo, //9
                        black_disp = black_disp, //10

                        challenger = challenger, //11
                        winner = winner, //12
                        turn = turn, //13
                        on_check = on_check, //14
                        highlight = highlight, //15
                        updated = updated
                    )
                    gameList.add(game)
                }
                line = fileReader.readLine()
            }
            for (game: Game in gameList) {
                repositoryGame.save(game)
            }
        }
    }

    private fun generateState(stateString: String): List<List<String>> {
        val representationString: List<String> = stateString.split("], [")
        if (representationString.size == 2) {
            val outputA: ArrayList<String> = arrayListOf()
            val rankA: List<String> = representationString[0].split(",")
            rankA.forEach {
                outputA.add(parseInvite(listElement = it))
            }
            val outputB: ArrayList<String> = arrayListOf()
            val rankB: List<String> = representationString[1].split(",")
            rankB.forEach {
                outputB.add(parseInvite(listElement = it))
            }
            return arrayListOf(outputA, outputB)
        }
        val output7: ArrayList<String> = arrayListOf()
        val rank7: List<String> = representationString[0].split(",")
        rank7.forEach {
            output7.add(parseName(listElement = it))
        }
        val output6: ArrayList<String> = arrayListOf()
        val rank6: List<String> = representationString[1].split(",")
        rank6.forEach {
            output6.add(parseName(listElement = it))
        }
        val output5: ArrayList<String> = arrayListOf()
        val rank5: List<String> = representationString[2].split(",")
        rank5.forEach {
            output5.add(parseName(listElement = it))
        }
        val output4: ArrayList<String> = arrayListOf()
        val rank4: List<String> = representationString[3].split(",")
        rank4.forEach {
            output4.add(parseName(listElement = it))
        }
        val output3: ArrayList<String> = arrayListOf()
        val rank3: List<String> = representationString[4].split(",")
        rank3.forEach {
            output3.add(parseName(listElement = it))
        }
        val output2: ArrayList<String> = arrayListOf()
        val rank2: List<String> = representationString[5].split(",")
        rank2.forEach {
            output2.add(parseName(listElement = it))
        }
        val output1: ArrayList<String> = arrayListOf()
        val rank1: List<String> = representationString[6].split(",")
        rank1.forEach {
            output1.add(parseName(listElement = it))
        }
        val output0: ArrayList<String> = arrayListOf()
        val rank0: List<String> = representationString[7].split(",")
        rank0.forEach {
            output0.add(parseName(listElement = it))
        }
        return arrayListOf(output7, output6, output5, output4, output3, output2, output1, output0)
    }

    private fun parseName(listElement: String): String {
        if (listElement.toLowerCase().contains("white")) {
            if (listElement.toLowerCase().contains("knight")) {
                return "KnightWhite"
            }
            if (listElement.toLowerCase().contains("bishop")) {
                return "BishopWhite"
            }
            if (listElement.toLowerCase().contains("rook")) {
                if (listElement.toLowerCase().contains("_x")) {
                    return "RookWhite_x"
                }
                return "RookWhite"
            }
            if (listElement.toLowerCase().contains("queen")) {
                return "QueenWhite"
            }
            if (listElement.toLowerCase().contains("king")) {
                if (listElement.toLowerCase().contains("_x")) {
                    return "KingWhite_x"
                }
                return "KingWhite"
            }
            if (listElement.toLowerCase().contains("grasshopper")) {
                return "GrasshopperWhite"
            }
            if (listElement.toLowerCase().contains("hunter")) {
                return "HunterWhite"
            }
            if (listElement.toLowerCase().contains("poison")) {
                if (listElement.toLowerCase().contains("_x")) {
                    return "PoisonWhite_x"
                }
                return "PoisonWhite"
            }
            if (listElement.toLowerCase().contains("pawn")) {
                if (listElement.toLowerCase().contains("_x")) {
                    return "PawnWhite_x"
                }
                return "PawnWhite"
            }
            if (listElement.toLowerCase().contains("amazon")) {
                return "AmazonWhite"
            }
            if (listElement.toLowerCase().contains("reveal")) {
                return "RevealWhite"
            }
        }
        if (listElement.toLowerCase().contains("knight")) {
            return "KnightBlack"
        }
        if (listElement.toLowerCase().contains("bishop")) {
            return "BishopBlack"
        }
        if (listElement.toLowerCase().contains("rook")) {
            if (listElement.toLowerCase().contains("_x")) {
                return "RookBlack_x"
            }
            return "RookBlack"
        }
        if (listElement.toLowerCase().contains("queen")) {
            return "QueenBlack"
        }
        if (listElement.toLowerCase().contains("king")) {
            if (listElement.toLowerCase().contains("_x")) {
                return "KingBlack_x"
            }
            return "KingBlack"
        }
        if (listElement.toLowerCase().contains("grasshopper")) {
            return "GrasshopperBlack"
        }
        if (listElement.toLowerCase().contains("hunter")) {
            return "HunterBlack"
        }
        if (listElement.toLowerCase().contains("poison")) {
            if (listElement.toLowerCase().contains("_x")) {
                return "PoisonBlack_x"
            }
            return "PoisonBlack"
        }
        if (listElement.toLowerCase().contains("pawn")) {
            if (listElement.toLowerCase().contains("_x")) {
                return "PawnBlack_x"
            }
            return "PawnBlack"
        }
        if (listElement.toLowerCase().contains("amazon")) {
            return "AmazonBlack"
        }
        if (listElement.toLowerCase().contains("reveal")) {
            return "RevealBlack"
        }
        return ""
    }

    private fun parseInvite(listElement: String): String {
        if (listElement.toLowerCase().contains("knight")) {
            return "Knight"
        }
        if (listElement.toLowerCase().contains("bishop")) {
            return "Bishop"
        }
        if (listElement.toLowerCase().contains("rook")) {
            return "Rook"
        }
        if (listElement.toLowerCase().contains("queen")) {
            return "Queen"
        }
        if (listElement.toLowerCase().contains("king")) {
            return "King"
        }
        if (listElement.toLowerCase().contains("grasshopper")) {
            return "Grasshopper"
        }
        if (listElement.toLowerCase().contains("hunter")) {
            return "Hunter"
        }
        if (listElement.toLowerCase().contains("poison")) {
            return "Poison"
        }
        if (listElement.toLowerCase().contains("pawn")) {
            return "Pawn"
        }
        if (listElement.toLowerCase().contains("amazon")) {
            return "Amazon"
        }
        return ""
    }
}