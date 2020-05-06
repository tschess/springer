package io.bahlsenwitz.springer.controller.game.backup

import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.generator.backup.Zipper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.File
import java.io.FileWriter
import java.io.IOException

class GameBackUp(private val repositoryGame: RepositoryGame) {

    private val HEADER = "" +
            "id;" + //         0
            "state;" + //      1
            "status;" + //     2
            "outcome;" + //    3
            "moves;" + //      4
            "white;" + //      5
            "white_elo;" + //  6
            "white_disp;" + // 7
            "black;" + //      8
            "black_elo;" + //  9
            "black_disp;" +// 10
            "challenger;" +// 11
            "winner;" + //    12
            "turn;" + //      13
            "on_check;" + //  14
            "highlight;" + // 15
            "updated" //      16

    fun backup(): ResponseEntity<Any> {
        val gameList: List<Game> = repositoryGame.findAll()

        val temp: File = File.createTempFile("game", ".csv")
        val fileWriter: FileWriter = FileWriter(temp)
        try {
            fileWriter.append("${HEADER}\n")

            for (game: Game in gameList) {
                val id: String = game.id.toString()
                fileWriter.append("${id};") //0

                var state: String = "NULL"
                if(game.state != null){ //toString()
                    state = game.state.toString()
                }
                fileWriter.append("${state};") //1

                val status: String = game.status.toString()
                fileWriter.append("${status};") //2
                val outcome: String = game.condition.toString() //3
                fileWriter.append("${outcome};")
                val moves: String = game.moves.toString()
                fileWriter.append("${moves};") //4
                val white_id: String = game.white.id.toString()
                fileWriter.append("${white_id};") //5
                val white_elo: String = game.white_elo.toString()
                fileWriter.append("${white_elo};") //6
                val white_disp_o: Int? = game.white_disp
                if (white_disp_o == null) {
                    fileWriter.append("NULL;") //7
                } else {
                    fileWriter.append("${game.white_disp.toString()};")
                }

                val black_id: String = game.black.id.toString()
                fileWriter.append("${black_id};") //8
                val black_elo: String = game.black_elo.toString()
                fileWriter.append("${black_elo};") //9
                val black_disp_o: Int? = game.black_disp
                if (black_disp_o == null) {
                    fileWriter.append("NULL;") //10
                } else {
                    fileWriter.append("${game.black_disp.toString()};")
                }

                val challenger: String = game.challenger.toString()  //11
                fileWriter.append("${challenger};")

                val winner_o: CONTESTANT? = game.winner
                if (winner_o == null) {
                    fileWriter.append("NULL;") //12
                } else {
                    fileWriter.append("${game.winner.toString()};")
                }
                val turn: String = game.turn.toString()
                fileWriter.append("${turn};") //13
                val on_check: Boolean = game.on_check
                fileWriter.append("${on_check};") //14
                val highlight: String = game.highlight
                fileWriter.append("${highlight};") //15
                val updated: String = game.updated
                fileWriter.append("${updated};") //16
                fileWriter.append('\n')
            }
        } finally {
            try {
                fileWriter.flush()
                fileWriter.close()
                Zipper().into(temp, name = "game")
                return ResponseEntity.status(HttpStatus.OK).body("{\"backup\":\"game\"}")
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"backup\":\"error\"}")
    }
}