package io.bahlsenwitz.springer.controller.game.backup

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.FileWriter

//@PostMapping("/backup")
class GameBackUp(private val repositoryGame: RepositoryGame) {

    private val DATE_TIME_GENERATOR = GeneratorDateTime()

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

        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter("${DATE_TIME_GENERATOR.rightNowString()}.game.csv")
            fileWriter.append("${csvHeader}\n")
            for (game in gameList) {

                val id: String = game.id.toString()
                fileWriter.append("${id};")
                val clock: String = game.clock.toString()
                fileWriter.append("${clock};")
                val state: String = game.state.toString()
                fileWriter.append("${state};")
                val highlight: String = game.highlight
                fileWriter.append("${highlight};")

                val whiteId: String = game.white.id.toString()
                fileWriter.append("${whiteId};")
                val whiteUpdate: String = game.white_update
                fileWriter.append("${whiteUpdate};")
                //
                val whiteMessage: String = game.white_message
                fileWriter.append("${whiteMessage};")
                val whitePosted: String = game.white_posted
                fileWriter.append("${whitePosted};")
                val whiteSeen: Boolean = game.white_seen
                fileWriter.append("${whiteSeen};")

                val blackId: String = game.black.id.toString()
                fileWriter.append("${blackId};")
                val blackUpdate: String = game.black_update
                fileWriter.append("${blackUpdate};")
                //
                val blackMessage: String = game.black_message
                fileWriter.append("${blackMessage};")
                val blackPosted: String = game.black_posted
                fileWriter.append("${blackPosted};")
                val blackSeen: Boolean = game.black_seen
                fileWriter.append("${blackSeen};")

                val config: String = game.config.toString()
                fileWriter.append("${config};")

                val checkOn: String = game.check_on
                fileWriter.append("${checkOn};")
                val turn: String = game.turn
                fileWriter.append("${turn};")
                val status: String = game.status
                fileWriter.append("${status};")

                val winner: String = game.winner
                fileWriter.append("${winner};")
                val catalyst: String = game.catalyst
                fileWriter.append("${catalyst};")
                val skin: String = game.skin
                fileWriter.append("${skin};")
                val updated: String = game.updated
                fileWriter.append("${updated};")
                val created: String = game.created
                fileWriter.append("${created};")
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