package io.bahlsenwitz.springer.controller.game.backup

import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

//@PostMapping("/backup")
class GameBackUp(private val repositoryGame: RepositoryGame) {

    fun backup(): ResponseEntity<Any> {
        val csvHeader = "" +
                "id;" + //         0
                "state;" + //      1
                "status;" + //     2
                "outcome;" + //    3
                "moves;" + //      4
                "white;" + //      5
                "white_elo;" + //  6
                "white_disp;" + // 7
                "white_skin;" + // 8
                "black;" + //      9
                "black_elo;" + // 10
                "black_disp;" +// 11
                "black_skin;" +// 12
                "challenger;" +// 13
                "winner;" + //    14
                "turn;" + //      15
                "on_check;" + //  16
                "highlight;" + // 17
                "updated;" + //   18
                "created" //      19
        val gameList: List<Game> = repositoryGame.findAll()

        val zonedDateTime: Date = Date.from(ZonedDateTime.now(ZoneId.of("America/New_York")).toInstant())
        val format = SimpleDateFormat("dd-MM-yyy")
        val date: String = format.format(zonedDateTime)

        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter("${date}_game.csv")
            fileWriter.append("${csvHeader}\n")
            for (game in gameList) {

                val id: String = game.id.toString()
                fileWriter.append("${id};") //0

                val state: String = game.state.toString()
                fileWriter.append("${state};") //1

                val status: String = game.status.toString()
                fileWriter.append("${status};") //2

                val outcome: String = game.outcome.toString() //3
                fileWriter.append("${outcome};")

                val moves: String = game.moves.toString()
                fileWriter.append("${moves};") //4

                val white_id: String = game.white.id.toString()
                fileWriter.append("${white_id};") //5
                val white_elo: String = game.white_elo.toString()
                fileWriter.append("${white_elo};") //6

                val white_disp_o: Int? = game.white_disp
                if(white_disp_o == null){
                    fileWriter.append("NULL;") //7
                } else {
                    fileWriter.append("${game.white_disp.toString()};")
                }

                val white_skin: String = game.white_skin.toString()
                fileWriter.append("${white_skin};") //8
                //
                val black_id: String = game.black.id.toString()
                fileWriter.append("${black_id};") //9
                val black_elo: String = game.black_elo.toString()
                fileWriter.append("${black_elo};") //10

                val black_disp_o: Int? = game.black_disp
                if(black_disp_o == null){
                    fileWriter.append("NULL;") //11
                } else {
                    fileWriter.append("${game.black_disp.toString()};")
                }

                val black_skin: String = game.black_skin.toString()
                fileWriter.append("${black_skin};") //12


                val challenger_o: CONTESTANT? = game.challenger
                if(challenger_o == null){
                    fileWriter.append("NULL;") //13
                } else {
                    fileWriter.append("${game.challenger.toString()};")
                }
                val winner_o: CONTESTANT? = game.winner
                if(winner_o == null){
                    fileWriter.append("NULL;") //14
                } else {
                    fileWriter.append("${game.winner.toString()};")
                }


                val turn: String = game.turn.toString()
                fileWriter.append("${turn};") //15

                val on_check: Boolean = game.on_check
                fileWriter.append("${on_check};") //16


                val highlight: String = game.highlight
                fileWriter.append("${highlight};") //17


                val updated: String = game.updated.toString()
                fileWriter.append("${updated};") //18

                val created: String = game.created.toString()
                fileWriter.append("${created};") //19

                fileWriter.append('\n')
            }
        } catch (exception: Exception) {
            println("Writing CSV error!")
            exception.printStackTrace()
        } finally {
            try {
                fileWriter!!.flush()
                fileWriter.close()
            } catch (exception: IOException) {
                println("Flushing/closing error!")
                exception.printStackTrace()
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"backup\":\"game\"}")
    }

}