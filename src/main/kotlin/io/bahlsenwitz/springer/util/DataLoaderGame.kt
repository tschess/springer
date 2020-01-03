package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.Game
import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class DataLoaderGame(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {
    private val IDX_ID = 0
    private val IDX_CLOCK = 1
    private val IDX_STATE = 2
    private val IDX_HIGHLIGHT = 3

    private val IDX_WHITE_ID = 4
    private val IDX_WHITE_UPDATE = 5
    private val IDX_BLACK_ID = 6
    private val IDX_BLACK_UPDATE = 7

    private val IDX_CONFIG = 8
    private val IDX_CHECK_ON = 9
    private val IDX_TURN = 10
    private val IDX_STATUS = 11
    private val IDX_WINNER = 12

    private val IDX_CATALYST = 13
    private val IDX_SKIN = 14
    private val IDX_UPDATED = 15
    private val IDX_CREATED = 16

    fun importDataGame() {
        repositoryGame.deleteAll()
        var fileReader: BufferedReader? = null
        try {
            val gameList = ArrayList<Game>()
            val dir = File(".")
            val file = dir.listFiles { _, name -> name.endsWith(".game.csv") }
            if(file?.count() != 1){
                return
            }
            fileReader = BufferedReader(FileReader(file[0]))
            fileReader.readLine()
            var line: String?
            line = fileReader.readLine()
            while (line != null) {
                val tokens = line.split(";")
                if (tokens.isNotEmpty()) {

                    val id = UUID.fromString(tokens[IDX_ID])
                    val clock = tokens[IDX_CLOCK].toInt()
                    val stateString = tokens[IDX_STATE] // handle...
                    val state = generateState(stateString=stateString) //???
                    val highlight = tokens[IDX_HIGHLIGHT]

                    val whiteIdString = tokens[IDX_WHITE_ID] // look up
                    val whiteId = UUID.fromString(whiteIdString)
                    val white: Player = repositoryPlayer.getById(whiteId)
                    val whiteUpdate = tokens[IDX_WHITE_UPDATE]

                    val blackIdString = tokens[IDX_BLACK_ID]
                    val blackId = UUID.fromString(blackIdString)
                    val black: Player = repositoryPlayer.getById(blackId)
                    val blackUpdate = tokens[IDX_BLACK_UPDATE]

                    val configString = tokens[IDX_CONFIG]
                    val config = generateConfig(configString=configString)

                    val checkOn = tokens[IDX_CHECK_ON]
                    val turn = tokens[IDX_TURN]
                    val status = tokens[IDX_STATUS]
                    val winner = tokens[IDX_WINNER]

                    val catalyst = tokens[IDX_CATALYST]
                    val skin = tokens[IDX_SKIN]
                    val updated = tokens[IDX_UPDATED]
                    val created = tokens[IDX_CREATED]
                    val game = Game(
                        id=id,
                        clock=clock,
                        state=state,
                        highlight=highlight,
                        white=white,
                        white_update=whiteUpdate,
                        black=black,
                        config=config,
                        black_update=blackUpdate,
                        check_on=checkOn,
                        turn=turn,
                        status=status,
                        winner=winner,
                        catalyst=catalyst,
                        skin=skin,
                        updated=updated,
                        created=created)
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

    private fun generateConfig(configString: String): List<List<String>> {

        val representationString: List<String> = configString.split("], [")
        val output1 = arrayListOf<String>()
        val rank1 = representationString[0].split(",")
        rank1.forEach {
            output1.add(parseName(listElement = it))
        }
        val output0 = arrayListOf<String>()
        val rank0 = representationString[1].split(",")
        rank0.forEach {
            output0.add(parseName(listElement = it))
        }
        return arrayListOf(output1, output0)
    }

    private fun generateState(stateString: String): List<List<String>> {
        val representationString: List<String> = stateString.split("], [")
        val output7 = arrayListOf<String>()
        val rank7 = representationString[0].split(",")
        rank7.forEach {
            output7.add(parseName(listElement=it))
        }
        val output6 = arrayListOf<String>()
        val rank6 = representationString[1].split(",")
        rank6.forEach {
            output6.add(parseName(listElement=it))
        }
        val output5 = arrayListOf<String>()
        val rank5 = representationString[2].split(",")
        rank5.forEach {
            output5.add(parseName(listElement=it))
        }
        val output4 = arrayListOf<String>()
        val rank4 = representationString[3].split(",")
        rank4.forEach {
            output4.add(parseName(listElement=it))
        }
        val output3 = arrayListOf<String>()
        val rank3 = representationString[4].split(",")
        rank3.forEach {
            output3.add(parseName(listElement=it))
        }
        val output2 = arrayListOf<String>()
        val rank2 = representationString[5].split(",")
        rank2.forEach {
            output2.add(parseName(listElement=it))
        }
        val output1 = arrayListOf<String>()
        val rank1 = representationString[6].split(",")
        rank1.forEach {
            output1.add(parseName(listElement=it))
        }
        val output0 = arrayListOf<String>()
        val rank0 = representationString[7].split(",")
        rank0.forEach {
            output0.add(parseName(listElement=it))
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
            if (listElement.toLowerCase().contains("landmine")) {
                return "WhiteLandminePawn"
            }
            if (listElement.toLowerCase().contains("arrow")) {
                return "WhiteArrowPawn"
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
        if (listElement.toLowerCase().contains("landmine")) {
            return "BlackLandminePawn"
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