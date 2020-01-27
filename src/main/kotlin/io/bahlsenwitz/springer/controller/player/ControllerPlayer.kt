package io.bahlsenwitz.springer.controller.player

import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.request.player.RequestLogin
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/player")
class ControllerPlayer(val repositoryPlayer: RepositoryPlayer) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody requestLogin: RequestLogin): ResponseEntity<Any> {
        val username: String = requestLogin.username
        val password: String = requestLogin.password
        val device: String = requestLogin.device
        val updated: String = requestLogin.updated

        val player = repositoryPlayer.getByUsername(username)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"nonexistent\"}")

        if (BCryptPasswordEncoder().matches(password, player.password)) {
            player.device = device
            player.updated = updated
            return ResponseEntity.ok(repositoryPlayer.save(player))
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"password\"}")
    }

    @PostMapping("/device/{device}")
    fun device(@PathVariable(value = "device") device: String): ResponseEntity<Any> {
        val player = repositoryPlayer.getByDevice(device)
            ?: return ResponseEntity.status(HttpStatus.OK).body("{\"info\": \"unassigned\"}")
        return ResponseEntity.ok(player)
    }

    @PostMapping("/leaderboard/{page}")
    fun leaderboard(@PathVariable(value = "page") page: Int): ResponseEntity<Any> {
        val PAGE_SIZE = 9

        val fromIndex: Int = page * PAGE_SIZE
        val toIndex: Int = fromIndex + PAGE_SIZE

        val playerList: List<Player> = repositoryPlayer.findAll(Sort.by("elo").descending())
        if(playerList.size < fromIndex) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"leaderboard\": \"EOL\"}")
        }
        if(playerList.size <= toIndex){
            val zzz = playerList.subList(fromIndex, playerList.lastIndex)
            return ResponseEntity.ok(zzz)
        }
        val mmm = playerList.subList(fromIndex, toIndex)
        return ResponseEntity.ok(mmm)
    }

    @PostMapping("/clear/{device}")
    fun clear(@PathVariable(value = "device") device: String): ResponseEntity<Any> {
        val player = repositoryPlayer.getByDevice(device)
        if(player != null){
            player.device = "TBD"
            return ResponseEntity.ok(repositoryPlayer.save(player))
        }
        return ResponseEntity.ok("{\"result\": \"ok\"}")
    }





//    @PostMapping("/create")
//    fun create(@Valid @RequestBody requestCreate: RequestCreate): ResponseEntity<Any> {
//        if (repositoryPlayer.getByName(requestCreate.name) != null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"reserved\"}")
//        }
//        val player = Player(
//            name = requestCreate.name,
//            password = BCryptPasswordEncoder().encode(requestCreate.password),
//            avatar = requestCreate.avatar,
//            device = requestCreate.device
//        )
//        val photoMap = PhotoGenerator().photoMap
//        val random = Random()
//        val avatar: String = photoMap.entries.elementAt(random.nextInt(photoMap.size)).value
//        player.avatar = avatar
//        player.api = requestCreate.api
//        player.updated = requestCreate.updated
//        player.created = requestCreate.created
//        player.rank = 1 + repositoryPlayer.findAll(Sort.by("elo").descending()).indexOf(player)
//        return ResponseEntity.ok(repositoryPlayer.save(player))
//    }

//    @PostMapping("/clear/{device}")
//    fun clear(@PathVariable(value = "device") device: String): ResponseEntity<Any> {
//        val player = repositoryPlayer.getByDevice(device)
//        if(player != null){
//            player.device = ""
//            return ResponseEntity.ok(repositoryPlayer.save(player))
//        }
//        return ResponseEntity.ok("{\"result\": \"ok\"}")
//    }

//    @PostMapping("/config")
//    fun config(@Valid @RequestBody updateConfig: UpdateConfig): ResponseEntity<Player> {
//        val id = UUID.fromString(updateConfig.id)!!
//        val player = repositoryPlayer.getById(id)
//        if(updateConfig.name == "config0"){
//            player.config0 = updateConfig.config
//        }
//        if(updateConfig.name == "config1"){
//            player.config1 = updateConfig.config
//        }
//        if(updateConfig.name == "config2"){
//            player.config2 = updateConfig.config
//        }
//        player.updated = updateConfig.updated
//        return ResponseEntity.ok(repositoryPlayer.save(player))
//    }

//    @GetMapping("/header/{id}")
//    fun header(@PathVariable(value = "id") id: String): ResponseEntity<Any> {
//        val player = repositoryPlayer.getById(UUID.fromString(id))
//        player.rank = 1 + repositoryPlayer.findAll(Sort.by("elo").descending()).indexOf(player)
//        repositoryPlayer.save(player)
//        return ResponseEntity.status(HttpStatus.OK).body("{" +
//                    "\"tschx\":${player.tschx}," +
//                    "\"rank\":${player.rank}," +
//                    "\"avatar\":\"${player.avatar}\"," +
//                    "\"address\":\"${player.address}\"}")
//    }

//    @PostMapping("/avatar")
//    fun avatar(@Valid @RequestBody updateAvatar: UpdateAvatar): ResponseEntity<Any> {
//        val player = repositoryPlayer.getById(UUID.fromString(updateAvatar.id))
//        player.avatar = updateAvatar.avatar
//        return ResponseEntity.ok(repositoryPlayer.save(player))
//    }
//
//    @PostMapping("/address")
//    fun ethereum(@Valid @RequestBody updateAddress: UpdateAddress): ResponseEntity<Any> {
//        val player = repositoryPlayer.getById(UUID.fromString(updateAddress.id))
//        player.address = updateAddress.address
//        return ResponseEntity.ok(repositoryPlayer.save(player))
//    }
//
//    @PostMapping("/skin")
//    fun skin(@Valid @RequestBody updateSkin: UpdateSkin): ResponseEntity<Any> {
//        val uuid = UUID.fromString(updateSkin.id)
//        val player = repositoryPlayer.getById(id = uuid)
//        player.skin = updateSkin.skin
//        player.updated = updateSkin.updated
//        return ResponseEntity.ok(repositoryPlayer.save(player))
//    }

}


















