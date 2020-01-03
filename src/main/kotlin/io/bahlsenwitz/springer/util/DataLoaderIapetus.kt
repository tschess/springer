package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.Iapetus
import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.repository.RepositoryIapetus
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class DataLoaderIapetus(
    private val repositoryIapetus: RepositoryIapetus,
    private val repositoryPlayer: RepositoryPlayer
) {
    private val IDX_ID = 0
    private val IDX_OWNER = 1
    private val IDX_NFT = 2
    private val IDX_UPDATED = 3

    fun importDataIapetus() {
        repositoryIapetus.deleteAll()
        var fileReader: BufferedReader? = null
        try {
            val iapetusList = ArrayList<Iapetus>()
            val dir = File(".")
            val file = dir.listFiles { _, name -> name.endsWith(".iapetus.csv") }
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
                    println("id: ${tokens[IDX_ID]}")
                    val id = UUID.fromString(tokens[IDX_ID])!!

                    println("owner: ${tokens[IDX_OWNER]}")
                    val ownerIdString = tokens[IDX_OWNER]
                    var owner: Player? = null
                    if(ownerIdString != ""){
                        val ownerId = UUID.fromString(ownerIdString)!!
                        owner = repositoryPlayer.getById(ownerId)
                    }

                    val nft = tokens[IDX_NFT]
                    val updated = tokens[IDX_UPDATED]
                    val iapetus = Iapetus(
                        id = id,
                        owner = owner,
                        nft = nft,
                        updated = updated)
                    iapetusList.add(iapetus)
                }
                line = fileReader.readLine()
            }
            for (iapetus in iapetusList) {
                repositoryIapetus.save(iapetus)
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
}
