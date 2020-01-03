package io.bahlsenwitz.springer.controller.player

import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.request.player.*
import io.bahlsenwitz.springer.util.PhotoGenerator
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Collections.singletonList
import java.util.stream.Collectors
import javax.validation.Valid

@RestController
@RequestMapping("/player")
class ControllerPlayer(val repositoryPlayer: RepositoryPlayer) {

    @PostMapping("/create")
    fun create(@Valid @RequestBody requestCreate: RequestCreate): ResponseEntity<Any> {
        if (repositoryPlayer.getByName(requestCreate.name) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"reserved\"}")
        }
        val player = Player(
            name = requestCreate.name,
            password = BCryptPasswordEncoder().encode(requestCreate.password),
            avatar = requestCreate.avatar,
            device = requestCreate.device
        )
        val photoMap = PhotoGenerator().photoMap
        val random = Random()
        val avatar: String = photoMap.entries.elementAt(random.nextInt(photoMap.size)).value
        player.avatar = avatar
        player.api = requestCreate.api
        player.updated = requestCreate.updated
        player.created = requestCreate.created
        player.rank = 1 + repositoryPlayer.findAll(Sort.by("elo").descending()).indexOf(player)
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody requestLogin: RequestLogin): ResponseEntity<Any> {
        val player = repositoryPlayer.getByName(requestLogin.name)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"nonexistent\"}")
        if (BCryptPasswordEncoder().matches(requestLogin.password, player.password)) {
            player.device = requestLogin.device
            repositoryPlayer.save(player)
            return ResponseEntity.ok(player)
        }
        player.api = requestLogin.api
        player.updated = requestLogin.updated
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"password\"}")
    }

    @PostMapping("/device")
    fun device(@Valid @RequestBody requestDevice: RequestDevice): ResponseEntity<Any> {
        val player = repositoryPlayer.getByDevice(requestDevice.device)
            ?: return ResponseEntity.status(HttpStatus.OK).body("{\"info\": \"unassigned\"}")
        player.updated = requestDevice.updated
        player.api = requestDevice.api
        repositoryPlayer.save(player)
        return ResponseEntity.ok(player)
    }

    @PostMapping("/clear/{device}")
    fun clear(@PathVariable(value = "device") device: String): ResponseEntity<Any> {
        val player = repositoryPlayer.getByDevice(device)
        if(player != null){
            player.device = ""
            return ResponseEntity.ok(repositoryPlayer.save(player))
        }
        return ResponseEntity.ok("{\"result\": \"ok\"}")
    }

    @GetMapping("/target/{name}")
    fun target(@PathVariable(value = "name") name: String): ResponseEntity<Any> {
        val player = repositoryPlayer.getByName(name)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"nonexistent\"}")
        return ResponseEntity.ok(player)
    }

    @PostMapping("/config")
    fun config(@Valid @RequestBody updateConfig: UpdateConfig): ResponseEntity<Player> {
        val id = UUID.fromString(updateConfig.id)!!
        val player = repositoryPlayer.getById(id)
        if(updateConfig.name == "config0"){
            player.config0 = updateConfig.config
        }
        if(updateConfig.name == "config1"){
            player.config1 = updateConfig.config
        }
        if(updateConfig.name == "config2"){
            player.config2 = updateConfig.config
        }
        player.updated = updateConfig.updated
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    @PostMapping("/squad")
    fun squad(@Valid @RequestBody updateSquad: UpdateSquad): ResponseEntity<Player> {
        val player = repositoryPlayer.getByName(updateSquad.name)!!
        val squad = ArrayList<String>()
        squad.addAll(player.squad)
        squad.addAll(singletonList(updateSquad.target))
        player.squad = squad
        player.tschx -= updateSquad.tschx
        player.updated = updateSquad.updated
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    @GetMapping("/header/{id}")
    fun header(@PathVariable(value = "id") id: String): ResponseEntity<Any> {
        val player = repositoryPlayer.getById(UUID.fromString(id))
        player.rank = 1 + repositoryPlayer.findAll(Sort.by("elo").descending()).indexOf(player)
        repositoryPlayer.save(player)
        return ResponseEntity.status(HttpStatus.OK).body("{" +
                    "\"tschx\":${player.tschx}," +
                    "\"rank\":${player.rank}," +
                    "\"avatar\":\"${player.avatar}\"," +
                    "\"address\":\"${player.address}\"}")
    }

    @PostMapping("/avatar")
    fun avatar(@Valid @RequestBody updateAvatar: UpdateAvatar): ResponseEntity<Any> {
        val player = repositoryPlayer.getById(UUID.fromString(updateAvatar.id))
        player.avatar = updateAvatar.avatar
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    @PostMapping("/address")
    fun ethereum(@Valid @RequestBody updateAddress: UpdateAddress): ResponseEntity<Any> {
        val player = repositoryPlayer.getById(UUID.fromString(updateAddress.id))
        player.address = updateAddress.address
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    @PostMapping("/skin")
    fun skin(@Valid @RequestBody updateSkin: UpdateSkin): ResponseEntity<Any> {
        val uuid = UUID.fromString(updateSkin.id)
        val player = repositoryPlayer.getById(id = uuid)
        player.skin = updateSkin.skin
        player.updated = updateSkin.updated
        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    @PostMapping("/leaderboard")
    fun leaderboard(@Valid @RequestBody requestLeaderboard: RequestLeaderboard): ResponseEntity<Any> {
        val pageRequest = PageRequest.of(requestLeaderboard.page, requestLeaderboard.size, Sort.by("elo").descending())
        return ResponseEntity.ok(repositoryPlayer.findAll(pageRequest))
    }

    @PostMapping("/backup")
    fun backup(): ResponseEntity<Any>  {
        val csvHeader = "" +
                "id," +
                "rank," +
                "address," +
                "api," +
                "avatar," +
                "config0," +
                "config1," +
                "config2," +
                "name," +
                "password," +
                "elo," +
                "tschx," +
                "squad," +
                "device," +
                "skin," +
                "updated," +
                "created"
        val playerList = repositoryPlayer.findAll()

        val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd")
        val timeZone = TimeZone.getTimeZone("America/New_York")
        simpleDateFormat.timeZone = timeZone
        val currentDate = simpleDateFormat.format(Date())

        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter("${currentDate}.player.csv")
            fileWriter.append("${csvHeader}\n")
            for (player in playerList) {
                val id: String = player.id.toString()
                fileWriter.append("${id};")
                val rank: String = player.rank.toString()
                fileWriter.append("${rank};")
                val address: String = player.address
                fileWriter.append("${address};")
                val api: String = player.api
                fileWriter.append("${api};")
                val avatar: String = player.avatar
                fileWriter.append("${avatar};")
                val config0: String = player.config0.toString()
                fileWriter.append("${config0};")
                val config1: String = player.config1.toString()
                fileWriter.append("${config1};")
                val config2: String = player.config2.toString()
                fileWriter.append("${config2};")
                val name: String = player.name
                fileWriter.append("${name};")
                val password: String = player.password
                fileWriter.append("${password};")
                val elo: String = player.elo.toString()
                fileWriter.append("${elo};")
                val tschx: String = player.tschx.toString()
                fileWriter.append("${tschx};")
                val squad: String = player.squad.toString()
                fileWriter.append("${squad};")
                val device: String = player.device
                fileWriter.append("${device};")
                val skin: String = player.skin
                fileWriter.append("${skin};")
                val updated: String = player.updated
                fileWriter.append("${updated};")
                val created: String = player.created
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
        return ResponseEntity.status(HttpStatus.OK).body("{\"backup\":\"player\"}")
    }

}


















