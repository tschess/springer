package io.bahlsenwitz.springer.controller.iapetus

import io.bahlsenwitz.springer.ethereum.EthPollingAgent
import io.bahlsenwitz.springer.repository.RepositoryIapetus
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.request.iapetus.UpdatePurchase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/iapetus")
class ControllerIapetus(
    val repositoryIapetus: RepositoryIapetus,
    val repositoryPlayer: RepositoryPlayer
) {

    @PostMapping("/purchase")
    fun purchase(@Valid @RequestBody updatePurchase: UpdatePurchase): ResponseEntity<Any> {
        val uuid = UUID.fromString(updatePurchase.id)
        val player = repositoryPlayer.getById(id = uuid)
        player.skin = "IAPETUS"
        player.updated = updatePurchase.updated
        repositoryPlayer.save(player)

        val iapetus = repositoryIapetus.getNext()
        iapetus.owner = player
        iapetus.updated = updatePurchase.updated
        return ResponseEntity.ok(repositoryIapetus.save(iapetus))
    }

    @PostMapping("/skins/{id}")
    fun skins(@PathVariable("id") id: String): ResponseEntity<Any> {
        val uuid = UUID.fromString(id)
        var default_skin = false
        val player = repositoryPlayer.getById(id=uuid)
        if (player.skin != "NONE") {
            default_skin = true
        }
        val iapetus = repositoryIapetus.getByOwnerId(owner=player)
        var steward = false
        if (iapetus != null) {
            steward = true
        }
        //If they've associated an eth account - it's something else
        //otherwise the same...
        if(player.address != "TBD"){
            val hasToken: Boolean = EthPollingAgent().hasToken(address=player.address)
            steward = hasToken
        }
        val count = repositoryIapetus.getRemaining()
        return ResponseEntity.status(HttpStatus.OK).body("{" +
                    "\"steward\":${steward}," +
                    "\"default_skin\":${default_skin}," +
                    "\"count\":${count}}")
    }

    @PostMapping("/backup")
    fun backup(): ResponseEntity<Any>  {
        val csvHeader = "id,owner,nft,updated"
        val iapetusList = repositoryIapetus.findAll()

        val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd")
        val timeZone = TimeZone.getTimeZone("America/New_York")
        simpleDateFormat.timeZone = timeZone
        val currentDate = simpleDateFormat.format(Date())

        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter("${currentDate}.iapetus.csv")
            fileWriter.append("${csvHeader}\n")
            for (iapetus in iapetusList) {
                val id: String = iapetus.id.toString()
                fileWriter.append("${id};")
                var owner = ""
                if (iapetus.owner != null) {
                    owner = iapetus.owner!!.id.toString()
                }
                fileWriter.append("${owner};")
                var nft = ""
                if (iapetus.nft != null) {
                    nft = iapetus.nft!!
                }
                fileWriter.append("${nft};")
                val updated: String = iapetus.updated
                fileWriter.append("${updated};")
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
        return ResponseEntity.status(HttpStatus.OK).body("{\"backup\":\"iapetus\"}")
    }
}