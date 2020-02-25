package io.bahlsenwitz.springer.controller.player.backup

import io.bahlsenwitz.springer.model.game.SKIN
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.ZipOutput
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.File
import java.io.FileWriter
import java.io.IOException

class PlayerBackUp(private val repositoryPlayer: RepositoryPlayer) {

    private val HEADER: String = "" +
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

    fun backup(): ResponseEntity<Any> {
        val playerList: List<Player> = repositoryPlayer.findAll()

        val temp: File = File.createTempFile("player", ".csv")
        val fileWriter: FileWriter = FileWriter(temp)
        try {
            fileWriter.append("${HEADER}\n")

            for (player: Player in playerList) {
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
                val date0: String = player.date.toString()
                fileWriter.append("${date0};") //7
                val note: Boolean = player.note
                fileWriter.append("${note};") //8
                val config0: String = player.config0.toString()
                fileWriter.append("${config0};") //9
                val config1: String = player.config1.toString()
                fileWriter.append("${config1};") //10
                val config2: String = player.config2.toString()
                fileWriter.append("${config2};") //11
                val skin: List<SKIN> = player.skin
                for ((index: Int, value: SKIN) in skin.withIndex()) {
                    if (index == skin.lastIndex) {
                        fileWriter.append("$value") //12...
                    } else {
                        fileWriter.append("${value},") //12...
                    }
                }
                fileWriter.append(";") //12
                val device: String? = player.device
                if (!device.isNullOrBlank()) {
                    fileWriter.append("${device};") //13
                } else {
                    fileWriter.append("NULL;") //13
                }
                val updated: String = player.updated.toString()
                fileWriter.append("${updated};") //14
                val created: String = player.created.toString()
                fileWriter.append("${created};") //15
                fileWriter.append('\n')
            }
        } finally {
            try {
                fileWriter.flush()
                fileWriter.close()
                ZipOutput().execute(temp, name = "player")
                return ResponseEntity.status(HttpStatus.OK).body("{\"backup\":\"player\"}")
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"backup\":\"error\"}")
    }

}