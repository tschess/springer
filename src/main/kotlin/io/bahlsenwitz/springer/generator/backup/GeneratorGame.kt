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
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {
    private val IDX_ID = 0
    private val IDX_STATE = 1
    private val IDX_STATUS = 2
    private val IDX_OUTCOME = 3
    private val IDX_MOVES = 4
    private val IDX_WHITE_ID = 5
    private val IDX_WHITE_ELO = 6
    private val IDX_WHITE_DISP = 7
    private val IDX_WHITE_SKIN = 8
    private val IDX_BLACK_ID = 9
    private val IDX_BLACK_ELO = 10
    private val IDX_BLACK_DISP = 11
    private val IDX_BLACK_SKIN = 12
    private val IDX_CHALLENGER = 13
    private val IDX_WINNER = 14
    private val IDX_TURN = 15
    private val IDX_ON_CHECK = 16
    private val IDX_HIGHLIGHT = 17
    private val IDX_UPDATED = 18
    private val IDX_CREATED = 19

    fun generate() {
        repositoryGame.deleteAll()
        var fileReader: BufferedReader? = null
        try {
            val gameList = ArrayList<Game>()
            val dir = File(".")
            val file = dir.listFiles { _, name -> name.endsWith(".game.csv") }
            if (file?.count() != 1) {
                return
            }
            fileReader = BufferedReader(FileReader(file[0]))
            fileReader.readLine()
            var line: String?
            line = fileReader.readLine()
            while (line != null) {
                val tokens = line.split(";")
                if (tokens.isNotEmpty()) {

                    val id: UUID = UUID.fromString(tokens[IDX_ID])!! //0
                    val stateString: String = tokens[IDX_STATE]
                    val state: List<List<String>> = generateState(stateString = stateString) //1
                    val status: STATUS = STATUS.valueOf(tokens[IDX_STATUS]) //2
                    val outcome: OUTCOME = OUTCOME.valueOf(tokens[IDX_OUTCOME]) //3
                    val moves: Int = tokens[IDX_MOVES].toInt() //4

                    val whiteIdString: String = tokens[IDX_WHITE_ID]
                    val white_id: UUID = UUID.fromString(whiteIdString)!!
                    val white: Player = repositoryPlayer.findById(white_id).get() //5
                    val whiteElo: Int = tokens[IDX_WHITE_ELO].toInt() //6
                    val whiteDisp: Int = tokens[IDX_WHITE_DISP].toInt() //7
                    val whiteSkin: SKIN = SKIN.valueOf(tokens[IDX_WHITE_SKIN]) //8
                    //
                    val blackIdString: String = tokens[IDX_BLACK_ID]
                    val black_id: UUID = UUID.fromString(blackIdString)!!
                    val black: Player = repositoryPlayer.findById(black_id).get() //9
                    val blackElo: Int = tokens[IDX_BLACK_ELO].toInt() //10
                    val blackDisp: Int = tokens[IDX_BLACK_DISP].toInt() //11
                    val blackSkin: SKIN = SKIN.valueOf(tokens[IDX_BLACK_SKIN]) //12

                    val challenger: CONTESTANT = CONTESTANT.valueOf(tokens[IDX_CHALLENGER]) //13
                    val winner: CONTESTANT = CONTESTANT.valueOf(tokens[IDX_WINNER]) //14
                    val turn: CONTESTANT = CONTESTANT.valueOf(tokens[IDX_TURN]) //15

                    val on_check: Boolean = tokens[IDX_ON_CHECK].toBoolean() //16
                    val highlight: String = tokens[IDX_HIGHLIGHT] //17
                    val updated: String = tokens[IDX_UPDATED] //18
                    val created: String = tokens[IDX_CREATED] //19
                    val game = Game(
                        id = id,
                        clock = clock,
                        state = state,
                        highlight = highlight,
                        white = white,
                        white_update = whiteUpdate,
                        black = black,
                        config = config,
                        black_update = blackUpdate,
                        check_on = checkOn,
                        turn = turn,
                        status = status,
                        winner = winner,
                        catalyst = catalyst,
                        skin = skin,
                        updated = updated,
                        created = created
                    )
                    gameList.add(game)
                }
                line = fileReader.readLine()
            }
            for (game in gameList) {
                repositoryGame.save(game)
            }
        } catch (e: Exception) {
            println("Reading CSV Error!")
            e.printStackTrace()
        } finally {
            try {
                fileReader!!.close()
            } catch (e: IOException) {
                println("Closing fileReader Error!")
                e.printStackTrace()
            }
        }
    }

    private fun generateState(stateString: String): List<List<String>> {
        val representationString: List<String> = stateString.split("], [")
        val output7 = arrayListOf<String>()
        val rank7 = representationString[0].split(",")
        rank7.forEach {
            output7.add(parseName(listElement = it))
        }
        val output6 = arrayListOf<String>()
        val rank6 = representationString[1].split(",")
        rank6.forEach {
            output6.add(parseName(listElement = it))
        }
        val output5 = arrayListOf<String>()
        val rank5 = representationString[2].split(",")
        rank5.forEach {
            output5.add(parseName(listElement = it))
        }
        val output4 = arrayListOf<String>()
        val rank4 = representationString[3].split(",")
        rank4.forEach {
            output4.add(parseName(listElement = it))
        }
        val output3 = arrayListOf<String>()
        val rank3 = representationString[4].split(",")
        rank3.forEach {
            output3.add(parseName(listElement = it))
        }
        val output2 = arrayListOf<String>()
        val rank2 = representationString[5].split(",")
        rank2.forEach {
            output2.add(parseName(listElement = it))
        }
        val output1 = arrayListOf<String>()
        val rank1 = representationString[6].split(",")
        rank1.forEach {
            output1.add(parseName(listElement = it))
        }
        val output0 = arrayListOf<String>()
        val rank0 = representationString[7].split(",")
        rank0.forEach {
            output0.add(parseName(listElement = it))
        }
        return arrayListOf(output7, output6, output5, output4, output3, output2, output1, output0)
    }

    private fun parseName(listElement: String): String {
        if (listElement.toLowerCase().contains("white")) {
            if (listElement.toLowerCase().contains("knight")) {
                return "WhiteKnight"
            }
            if (listElement.toLowerCase().contains("bishop")) {
                return "WhiteBishop"
            }
            if (listElement.toLowerCase().contains("rook")) {
                return "WhiteRook"
            }
            if (listElement.toLowerCase().contains("queen")) {
                return "WhiteQueen"
            }
            if (listElement.toLowerCase().contains("king")) {
                return "WhiteKing"
            }
            if (listElement.toLowerCase().contains("grasshopper")) {
                return "WhiteGrasshopper"
            }
            if (listElement.toLowerCase().contains("hunter")) {
                return "WhiteHunter"
            }
            if (listElement.toLowerCase().contains("poison")) {
                return "WhitePoisonPawn"
            }
            if (listElement.toLowerCase().contains("pawn")) {
                return "WhitePawn"
            }
            if (listElement.toLowerCase().contains("amazon")) {
                return "WhiteAmazon"
            }
        }
        if (listElement.toLowerCase().contains("knight")) {
            return "BlackKnight"
        }
        if (listElement.toLowerCase().contains("bishop")) {
            return "BlackBishop"
        }
        if (listElement.toLowerCase().contains("rook")) {
            return "BlackRook"
        }
        if (listElement.toLowerCase().contains("queen")) {
            return "BlackQueen"
        }
        if (listElement.toLowerCase().contains("king")) {
            return "BlackKing"
        }
        if (listElement.toLowerCase().contains("grasshopper")) {
            return "BlackGrasshopper"
        }
        if (listElement.toLowerCase().contains("hunter")) {
            return "BlackHunter"
        }
        if (listElement.toLowerCase().contains("poison")) {
            return "BlackPoisonPawn"
        }
        if (listElement.toLowerCase().contains("arrow")) {
            return "BlackArrowPawn"
        }
        if (listElement.toLowerCase().contains("pawn")) {
            return "BlackPawn"
        }
        if (listElement.toLowerCase().contains("amazon")) {
            return "BlackAmazon"
        }
        return ""
    }
}