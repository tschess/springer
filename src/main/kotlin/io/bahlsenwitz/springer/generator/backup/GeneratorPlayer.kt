package io.bahlsenwitz.springer.generator.backup


import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

import java.util.*
import kotlin.collections.ArrayList

class GeneratorPlayer(private val repositoryPlayer: RepositoryPlayer) {
    private val IDX_ID = 0
    private val IDX_USERNAME = 1
    private val IDX_PASSWORD = 2
    private val IDX_AVATAR = 3
    private val IDX_ELO = 4
    private val IDX_RANK = 5
    private val IDX_DISP = 6
    private val IDX_DATE = 7
    private val IDX_NOTE_VALUE = 8 //TODO: !!! attend to this on migration
    //private val IDX_NOTE_KEY = 9 //TODO: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    //private val IDX_CONFIG0 = 10
    //private val IDX_CONFIG1 = 11
    //private val IDX_CONFIG2 = 12
    //private val IDX_DEVICE = 13
    //private val IDX_UPDATED = 14
    //private val IDX_CREATED = 15

    private val IDX_CONFIG0 = 9
    private val IDX_CONFIG1 = 10
    private val IDX_CONFIG2 = 11
    private val IDX_DEVICE = 12
    private val IDX_UPDATED = 13
    private val IDX_CREATED = 14

    fun generate(file: File) {
        repositoryPlayer.deleteAll()
        val playerList = ArrayList<Player>()

        val bufferedReader: BufferedReader = BufferedReader(FileReader(file))
        bufferedReader.use { fileReader ->
            fileReader.readLine()
            var line: String?
            line = fileReader.readLine()
            while (line != null) {
                val tokens: List<String> = line.split(";")
                if (tokens.isNotEmpty()) {
                    val id: UUID = UUID.fromString(tokens[IDX_ID])!! //0
                    val username: String = tokens[IDX_USERNAME] //1
                    val password: String = tokens[IDX_PASSWORD] //2
                    val avatar: String = tokens[IDX_AVATAR] //3
                    val elo: Int = tokens[IDX_ELO].toInt() //4
                    val rank: Int = tokens[IDX_RANK].toInt() //5
                    val disp: Int = tokens[IDX_DISP].toInt() //6
                    val date: String = tokens[IDX_DATE] //7
                    val note_value: Boolean = tokens[IDX_NOTE_VALUE].toBoolean() //8

                    val note_key: String? = null
                    //var note_key: String? = tokens[IDX_NOTE_KEY] //9
                    //note_key = if(note_key == "NULL"){
                        //null
                    //} else {
                        //note_key.toString()
                    //}

                    val configString0: String = tokens[IDX_CONFIG0] //10
                    val config0: List<List<String>> = generateConfig(configString = configString0)
                    val configString1: String = tokens[IDX_CONFIG1] //11
                    val config1: List<List<String>> = generateConfig(configString = configString1)
                    val configString2: String = tokens[IDX_CONFIG2] //12
                    val config2: List<List<String>> = generateConfig(configString = configString2)
                    var device: String? = tokens[IDX_DEVICE] //13
                    device = if(device == "NULL"){
                        null
                    } else {
                        device.toString()
                    }
                    val updated: String = tokens[IDX_UPDATED] //14
                    val created: String = tokens[IDX_CREATED] //15

                    val player = Player(
                        id = id, //0
                        username = username, //1
                        password = password, //2
                        avatar = avatar, //3
                        elo = elo, //4
                        rank = rank, //5
                        disp = disp, //6
                        date = date, //7
                        note_value = note_value, //8
                        note_key = note_key, //9
                        config0 = config0, //10
                        config1 = config1, //11
                        config2 = config2, //12

                        device = device, //13
                        updated = updated, //14
                        created = created //15
                    )
                    playerList.add(player)
                }
                line = fileReader.readLine()
            }
            for (player: Player in playerList) {
                repositoryPlayer.save(player)
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

    private fun parseName(listElement: String): String {
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