package io.bahlsenwitz.springer.controller.player

import io.bahlsenwitz.springer.controller.player.backup.PlayerBackUp
import io.bahlsenwitz.springer.controller.player.path.PlayerInit
import io.bahlsenwitz.springer.controller.player.request.PlayerConfig
import io.bahlsenwitz.springer.controller.player.request.*
import io.bahlsenwitz.springer.controller.player.path.PlayerRivals
import io.bahlsenwitz.springer.controller.player.request.PlayerPush
import io.bahlsenwitz.springer.controller.player.util.PlayerProfile
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@SpringBootApplication(scanBasePackages = ["io.bahlsenwitz.springer"])
@RestController
@RequestMapping("/player")
class PlayerController @Autowired
constructor(val repositoryPlayer: RepositoryPlayer, val repositoryGame: RepositoryGame) {

    @PostMapping("/leaderboard")
    fun leaderboard(@Valid @RequestBody requestPage: PlayerHome.RequestPage): ResponseEntity<Any> {
        return PlayerHome(repositoryPlayer).leaderboard(requestPage)
    }

    @PostMapping("/rivals/{id}")
    fun rivals(@PathVariable(value = "id") id: String): ResponseEntity<Any> {
        return PlayerRivals(repositoryPlayer).rivals(id)
    }

    @PostMapping("/device/{device}")
    fun device(@PathVariable(value = "device") device: String): ResponseEntity<Any> {
        return PlayerInit(repositoryPlayer).device(device)
    }

    val playerStart = PlayerStart(repositoryPlayer, repositoryGame)

    @PostMapping("/create")
    fun create(@Valid @RequestBody requestCreate: PlayerStart.RequestStart): ResponseEntity<Any> {
        return playerStart.create(requestCreate)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody requestLogin: PlayerStart.RequestStart): ResponseEntity<Any> {
        return playerStart.login(requestLogin)
    }

    val playerProfile = PlayerProfile(repositoryPlayer)

    @PostMapping("/clear/{device}")
    fun clear(@PathVariable(value = "device") device: String): ResponseEntity<Any> {
        return playerProfile.clear(device)
    }

    @PostMapping("/avatar")
    fun avatar(@Valid @RequestBody updateAvatar: PlayerProfile.UpdateAvatar): ResponseEntity<Any> {
        return playerProfile.avatar(updateAvatar)
    }

    @PostMapping("/config")
    fun config(@Valid @RequestBody updateConfig: PlayerConfig.UpdateConfig): ResponseEntity<Any> {
        return PlayerConfig(repositoryPlayer).config(updateConfig)
    }

    @PostMapping("/push")
    fun push(@Valid @RequestBody updatePush: PlayerPush.UpdatePush): ResponseEntity<Any> {
        return PlayerPush(repositoryPlayer).push(updatePush)
    }

    @PostMapping("/backup")
    fun backup(): Any {
        return PlayerBackUp(repositoryPlayer).backup()
    }

}


















