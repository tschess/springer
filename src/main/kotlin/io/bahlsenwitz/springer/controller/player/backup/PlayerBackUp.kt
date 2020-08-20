package io.bahlsenwitz.springer.controller.player.backup

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.generator.backup.Zipper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.File
import java.io.FileWriter
import java.io.IOException

class PlayerBackUp(private val repositoryPlayer: RepositoryPlayer) {

    private val HEADER: String = "" +
            "id;" + //         00
            "username;" + //   01
            "password;" + //   02
            "avatar;" + //     03
            "elo;" + //        04
            "rank;" + //       05
            "disp;" + //       06
            "date;" + //       07
            "note_value;" + // 08  //TODO: !!!
            "note_key;" + //   09  //TODO: ^^
            "config0;" + //     10
            "config1;" + //     11
            "config2;" + //     12
            "device;" + //     13
            "updated;" + //    14
            "created" //       15

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

                val note_value: Boolean = player.note_value
                fileWriter.append("${note_value};") //8

                val note_key: String? = player.note_key
                if (!note_key.isNullOrBlank()) {
                    fileWriter.append("${note_key};") //9
                } else {
                    fileWriter.append("NULL;") //9
                }

                val config0: String = player.config0.toString()
                fileWriter.append("${config0};") //10
                val config1: String = player.config1.toString()
                fileWriter.append("${config1};") //11
                val config2: String = player.config2.toString()
                fileWriter.append("${config2};") //12
                val device: String? = player.device
                if (!device.isNullOrBlank()) {
                    fileWriter.append("${device};") //13
                } else {
                    fileWriter.append("NULL;") //13
                }
                val updated: String = player.updated
                fileWriter.append("${updated};") //14
                val created: String = player.created
                fileWriter.append("${created};") //15
                fileWriter.append('\n')
            }
        } finally {
            try {
                fileWriter.flush()
                fileWriter.close()
                Zipper().into(temp, name = "player")
                return ResponseEntity.status(HttpStatus.OK).body("{\"backup\":\"player\"}")
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"backup\":\"error\"}")
    }

}