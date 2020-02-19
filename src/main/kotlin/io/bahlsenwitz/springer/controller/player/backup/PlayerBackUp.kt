package io.bahlsenwitz.springer.controller.player.backup

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.game.SKIN
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.FileWriter
import java.io.IOException

class PlayerBackUp(private val repositoryPlayer: RepositoryPlayer) {

    private val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun backup(): ResponseEntity<Any> {
        val csvHeader: String = "" +
                "id;" + //       0
                "username;" + // 1
                "password;" + // 2
                "avatar;" + //   3
                "elo;" + //      4
                "rank;" + //     5
                "disp;" + //     6
                "date;" + //     7
                "note;" + //   8
                "config0;" + //  9
                "config1;" + //  10
                "config2;" + //  11
                "skin;" + //     12
                "device;" + //   13
                "updated;" + //  14
                "created" //     15
        val playerList: List<Player> = repositoryPlayer.findAll()

        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter("${DATE_TIME_GENERATOR.rightNowString()}.player.csv")
            fileWriter.append("${csvHeader}\n")
            for (player in playerList) {
                val id: String = player.id.toString()
                fileWriter.append("${id};") //0

                val username: String = player.username
                fileWriter.append("${username};") //1

                val password: String = player.password
                fileWriter.append("${password};") //2

                val avatar: String = player.avatar
                fileWriter.append("${avatar};") //3

                val elo: String = player.elo.toString()
                fileWriter.append("${elo};") //4
                val rank: String = player.rank.toString()
                fileWriter.append("${rank};") //5
                val disp: String = player.disp.toString()
                fileWriter.append("${disp};") //6
                val date: String = player.date
                fileWriter.append("${date};") //7

                val note: Boolean = player.note
                fileWriter.append("${note};") //8

                val config0: String = player.config0.toString()
                fileWriter.append("${config0};") //9
                val config1: String = player.config1.toString()
                fileWriter.append("${config1};") //10
                val config2: String = player.config2.toString()
                fileWriter.append("${config2};") //11


                val squad: String = player.skin.toString()
                fileWriter.append("${squad};") //12


                val device: String? = player.device
                if (!device.isNullOrBlank()) {
                    fileWriter.append("${device};") //13
                } else {
                    fileWriter.append("NULL;") //13
                }

                val skin: List<SKIN> = player.skin
                fileWriter.append("${skin};") //14


                val updated: String = player.updated
                fileWriter.append("${updated};") //15
                val created: String = player.created
                fileWriter.append("${created};") //16
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
        return ResponseEntity.status(HttpStatus.OK).body("{\"backup\":\"player\"}")
    }

}