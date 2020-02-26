package io.bahlsenwitz.springer.generator.backup

import io.bahlsenwitz.springer.model.game.*
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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

    fun generate(file: File) {
        repositoryGame.deleteAll()
        val gameList = ArrayList<Game>()

        val fileReader: BufferedReader = BufferedReader(FileReader(file))
        try {
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
                    //print("\nwhite_id ${white_id}\n")
                    val white: Player = repositoryPlayer.findById(white_id).get() //5
                    val white_elo: Int = tokens[IDX_WHITE_ELO].toInt() //6

                    var white_disp: Int? = null
                    val white_disp_0: String = tokens[IDX_WHITE_DISP] //7
                    if(white_disp_0 != "NULL"){
                        white_disp = white_disp_0.toInt()
                    }

                    val white_skin: SKIN = SKIN.valueOf(tokens[IDX_WHITE_SKIN]) //8
                    val blackIdString: String = tokens[IDX_BLACK_ID]
                    val black_id: UUID = UUID.fromString(blackIdString)!!
                    val black: Player = repositoryPlayer.findById(black_id).get() //9
                    val black_elo: Int = tokens[IDX_BLACK_ELO].toInt() //10

                    var black_disp: Int? = null
                    val black_disp_0: String = tokens[IDX_BLACK_DISP] //11
                    if(black_disp_0 != "NULL"){
                        black_disp = black_disp_0.toInt()
                    }

                    val black_skin: SKIN = SKIN.valueOf(tokens[IDX_BLACK_SKIN]) //12
                    //val challenger: CONTESTANT = CONTESTANT.valueOf(tokens[IDX_CHALLENGER]) //13

                    var challenger: CONTESTANT? = null
                    val challenger_0: String = tokens[IDX_CHALLENGER] //11
                    if(challenger_0 != "NULL"){
                        challenger = CONTESTANT.valueOf(challenger_0)
                    }
                    var winner: CONTESTANT? = null
                    val winner_0: String = tokens[IDX_WINNER] //14
                    if(winner_0 != "NULL"){
                        winner = CONTESTANT.valueOf(winner_0)
                    }
                    //dd.MM.yyyy_HH:mm:ss.SSSS
                    val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    val BROOKLYN = ZoneId.of("America/New_York")

                    val turn: CONTESTANT = CONTESTANT.valueOf(tokens[IDX_TURN]) //15
                    val on_check: Boolean = tokens[IDX_ON_CHECK].toBoolean() //16
                    val highlight: String = tokens[IDX_HIGHLIGHT] //17
                    val updated: String = tokens[IDX_UPDATED] //18
                    val created: String = tokens[IDX_CREATED] //19
                    val game = Game(
                        id = id, //0
                        state = state, //1
                        status = status, //2
                        outcome = outcome, //3
                        moves = moves, //4
                        white = white, //5
                        white_elo = white_elo, //6
                        white_disp = white_disp, //7
                        white_skin = white_skin, //8
                        black = black, //9
                        black_elo = black_elo, //10
                        black_disp = black_disp, //11
                        black_skin = black_skin, //12
                        challenger = challenger, //13
                        winner = winner, //14
                        turn = turn, //15
                        on_check = on_check, //16
                        highlight = highlight, //17
                        updated = Date.from(LocalDateTime.parse(updated, FORMATTER).atZone(BROOKLYN).toInstant()), //18
                        created = Date.from(LocalDateTime.parse(created, FORMATTER).atZone(BROOKLYN).toInstant()) //19
                    )
                    gameList.add(game)
                }
                line = fileReader.readLine()
            }
            for (game: Game in gameList) {
                repositoryGame.save(game)
            }
        } finally {
            fileReader.close()
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
                return "KnightWhite"
            }
            if (listElement.toLowerCase().contains("bishop")) {
                return "BishopWhite"
            }
            if (listElement.toLowerCase().contains("rook")) {
                return "RookWhite"
            }
            if (listElement.toLowerCase().contains("queen")) {
                return "QueenWhite"
            }
            if (listElement.toLowerCase().contains("king")) {
                return "KingWhite"
            }
            if (listElement.toLowerCase().contains("grasshopper")) {
                return "GrasshopperWhite"
            }
            if (listElement.toLowerCase().contains("hunter")) {
                return "HunterWhite"
            }
            if (listElement.toLowerCase().contains("poison")) {
                return "PoisonPawnWhite"
            }
            if (listElement.toLowerCase().contains("pawn")) {
                return "PawnWhite"
            }
            if (listElement.toLowerCase().contains("amazon")) {
                return "AmazonWhite"
            }
        }
        if (listElement.toLowerCase().contains("knight")) {
            return "KnightBlack"
        }
        if (listElement.toLowerCase().contains("bishop")) {
            return "BishopBlack"
        }
        if (listElement.toLowerCase().contains("rook")) {
            return "RookBlack"
        }
        if (listElement.toLowerCase().contains("queen")) {
            return "QueenBlack"
        }
        if (listElement.toLowerCase().contains("king")) {
            return "KingBlack"
        }
        if (listElement.toLowerCase().contains("grasshopper")) {
            return "GrasshopperBlack"
        }
        if (listElement.toLowerCase().contains("hunter")) {
            return "HunterBlack"
        }
        if (listElement.toLowerCase().contains("poison")) {
            return "PoisonPawnBlack"
        }
        if (listElement.toLowerCase().contains("pawn")) {
            return "PawnBlack"
        }
        if (listElement.toLowerCase().contains("amazon")) {
            return "AmazonBlack"
        }
        return ""
    }
}