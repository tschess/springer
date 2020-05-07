package io.bahlsenwitz.springer.controller.player

import io.bahlsenwitz.springer.controller.player.backup.PlayerBackUp
import io.bahlsenwitz.springer.controller.player.board.PlayerHome
import io.bahlsenwitz.springer.controller.player.board.quick.PlayerQuick
import io.bahlsenwitz.springer.controller.player.start.PlayerCreate
import io.bahlsenwitz.springer.controller.player.start.PlayerLogin
import io.bahlsenwitz.springer.controller.player.start.RequestStart
import io.bahlsenwitz.springer.controller.player.start.PlayerInit
import io.bahlsenwitz.springer.controller.player.update.PlayerConfig
import io.bahlsenwitz.springer.controller.player.update.PlayerProfile
import io.bahlsenwitz.springer.controller.player.update.PlayerRefresh
import io.bahlsenwitz.springer.controller.player.update.polling.PlayerNotify
import io.bahlsenwitz.springer.model.player.Player
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
class ControllerPlayer @Autowired
constructor(repositoryPlayer: RepositoryPlayer, repositoryGame: RepositoryGame) {

    /**
     * Initializer.swift
     */
    val playerInit = PlayerInit(repositoryPlayer)

    @PostMapping("/device/{device}")
    fun device(@PathVariable(value = "device") device: String): ResponseEntity<Any> {
        return playerInit.device(device)
    }

    /**
     * Profile.swift
     */
    val playerProfile = PlayerProfile(repositoryPlayer)

    @PostMapping("/clear/{device}")
    fun clear(@PathVariable(value = "device") device: String): ResponseEntity<Any> {
        return playerProfile.clear(device)
    }

    @PostMapping("/avatar")
    fun avatar(@Valid @RequestBody updateAvatar: PlayerProfile.UpdateAvatar): ResponseEntity<Any> {
        return playerProfile.avatar(updateAvatar)
    }

    /**
     * Start.swift
     */

    val playerCreate = PlayerCreate(repositoryPlayer, repositoryGame)

    @PostMapping("/create")
    fun create(@Valid @RequestBody requestCreate: RequestStart): ResponseEntity<Any> {
        return playerCreate.create(requestCreate)
    }

    val playerStart = PlayerLogin(repositoryPlayer)

    @PostMapping("/login")
    fun login(@Valid @RequestBody requestLogin: RequestStart): ResponseEntity<Any> {
        return playerStart.login(requestLogin)
    }

    /**
     * Config.swift
     */
    val playerConfig = PlayerConfig(repositoryPlayer)

    @PostMapping("/config")
    fun config(@Valid @RequestBody updateConfig: PlayerConfig.UpdateConfig): ResponseEntity<Player> {
        return playerConfig.config(updateConfig)
    }

    /**
     * Quick.swift
     */
    val playerQuick = PlayerQuick(repositoryPlayer)

    @GetMapping("/quick/{id}")
    fun quick(@PathVariable(value = "id") id: String): ResponseEntity<Player> {
        return playerQuick.quick(id)
    }

    /**
     * Home.swift
     */
    val playerHome = PlayerHome(repositoryPlayer)

    @PostMapping("/leaderboard")
    fun leaderboard(@Valid @RequestBody requestPage: PlayerHome.RequestPage): ResponseEntity<Any> {
        return playerHome.leaderboard(requestPage)
    }

    val playerRefresh = PlayerRefresh(repositoryPlayer)

    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody requestRefresh: PlayerRefresh.RequestRefresh): ResponseEntity<Any> {
        return playerRefresh.refresh(requestRefresh)
    }

    val playerNotify = PlayerNotify(repositoryPlayer)

    @GetMapping("/notify/{id}")
    fun notify(@PathVariable(value = "id") id: String): Any {
        return playerNotify.notify(id)
    }

    val playerBackUp = PlayerBackUp(repositoryPlayer)

    @PostMapping("/backup")
    fun backup(): Any {
        return playerBackUp.backup()
    }

}


















