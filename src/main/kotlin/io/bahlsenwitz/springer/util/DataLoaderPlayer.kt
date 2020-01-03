package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class DataLoaderPlayer(private val repositoryPlayer: RepositoryPlayer) {
    private val IDX_ID = 0
    private val IDX_RANK = 1
    private val IDX_ADDRESS = 2
    private val IDX_API = 3
    private val IDX_AVATAR = 4
    private val IDX_CONFIG0 = 5
    private val IDX_CONFIG1 = 6
    private val IDX_CONFIG2 = 7
    private val IDX_NAME = 8
    private val IDX_PASSWORD = 9
    private val IDX_ELO = 10
    private val IDX_TSCHX = 11
    private val IDX_SQUAD = 12
    private val IDX_DEVICE = 13
    private val IDX_SKIN = 14
    private val IDX_UPDATED = 15
    private val IDX_CREATED = 16

    fun importDataPlayer() {
        repositoryPlayer.deleteAll()

        var fileReader: BufferedReader? = null
        try {
            val playerList = ArrayList<Player>()

            val dir = File(".")
            val file = dir.listFiles { _, name -> name.endsWith(".player.csv") }
            if(file?.count() != 1){
                return
            }
            fileReader = BufferedReader(FileReader(file[0]))
            // Read CSV header
            fileReader.readLine()
            // Read the file line by line starting from the second line
            var line: String?
            line = fileReader.readLine()

            while (line != null) {
                val tokens = line.split(";")
                if (tokens.isNotEmpty()) {
                    println("id: ${tokens[IDX_ID]}")
                    val id = UUID.fromString(tokens[IDX_ID])
                    val rank = tokens[IDX_RANK].toInt()
                    val address = tokens[IDX_ADDRESS]
                    val api = tokens[IDX_API]
                    val avatar = tokens[IDX_AVATAR]

                    val configString0 = tokens[IDX_CONFIG0]
                    val config0 = generateConfig(configString = configString0)
                    val configString1 = tokens[IDX_CONFIG1]
                    val config1 = generateConfig(configString = configString1)
                    val configString2 = tokens[IDX_CONFIG2]
                    val config2 = generateConfig(configString = configString2)

                    val name = tokens[IDX_NAME]
                    val password = tokens[IDX_PASSWORD]

                    val elo = tokens[IDX_ELO].toInt()
                    val tschx = tokens[IDX_TSCHX].toInt()

                    val squadString = tokens[IDX_SQUAD]
                    val squad = generateFairyList(squadString = squadString)

                    val device = tokens[IDX_DEVICE]
                    val skin = tokens[IDX_SKIN]
                    val updated = tokens[IDX_UPDATED]
                    val created = tokens[IDX_CREATED]
                    val player = Player(
                        id = id,
                        rank = rank,
                        address = address,
                        api = api,
                        avatar = avatar,
                        config0 = config0,
                        config1 = config1,
                        config2 = config2,
                        name = name,
                        password = password,
                        elo = elo,
                        tschx = tschx,
                        squad = squad,
                        device = device,
                        skin = skin,
                        updated = updated,
                        created = created
                    )
                    playerList.add(player)
                }
                line = fileReader.readLine()
            }
            for (player in playerList) {
                repositoryPlayer.save(player)
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

    private fun generateFairyList(squadString: String): List<String> {
        val output = arrayListOf<String>()
        val fairyList = squadString.split(",")
        fairyList.forEach {
            output.add(parseName(listElement = it))
        }
        return output
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
        if (listElement.toLowerCase().contains("landmine")) {
            return "LandminePawn"
        }
        if (listElement.toLowerCase().contains("arrow")) {
            return "ArrowPawn"
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
