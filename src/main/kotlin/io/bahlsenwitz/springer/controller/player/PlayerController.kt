package io.bahlsenwitz.springer.controller.player

import io.bahlsenwitz.springer.controller.player.backup.PlayerBackUp
import io.bahlsenwitz.springer.controller.player.path.PlayerInit
import io.bahlsenwitz.springer.controller.player.request.PlayerConfig
import io.bahlsenwitz.springer.controller.player.path.PlayerQuick
import io.bahlsenwitz.springer.controller.player.request.*
import io.bahlsenwitz.springer.controller.player.path.PlayerNotify
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
constructor(repositoryPlayer: RepositoryPlayer, repositoryGame: RepositoryGame) {

    /**
     * Start.swift
     */

    val playerStart = PlayerStart(repositoryPlayer, repositoryGame)

    @PostMapping("/create")
    fun create(@Valid @RequestBody requestCreate: PlayerStart.RequestStart): ResponseEntity<Any> {
        return playerStart.create(requestCreate)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody requestLogin: PlayerStart.RequestStart): ResponseEntity<Any> {
        return playerStart.login(requestLogin)
    }

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
     * Config.swift
     */
    val playerConfig = PlayerConfig(repositoryPlayer)

    @PostMapping("/config")
    fun config(@Valid @RequestBody updateConfig: PlayerConfig.UpdateConfig): ResponseEntity<Any> {
        return playerConfig.config(updateConfig)
    }

    /**
     * Quick.swift
     */
    val playerQuick = PlayerQuick(repositoryPlayer)
    @GetMapping("/quick/{id}")
    fun quick(@PathVariable(value = "id") id: String): ResponseEntity<Any> {
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

    val playerRefresh =
        PlayerRefresh(repositoryPlayer)

    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody requestRefresh: PlayerRefresh.RequestRefresh): ResponseEntity<Any> {
        return playerRefresh.refresh(requestRefresh)
    }

    val playerNotify = PlayerNotify(repositoryPlayer)

    @GetMapping("/notify/{id}")
    fun notify(@PathVariable(value = "id") id: String): ResponseEntity<Any>? {
        return playerNotify.notify(id)
    }

    val playerBackUp = PlayerBackUp(repositoryPlayer)

    @PostMapping("/backup")
    fun backup(): Any {
        return playerBackUp.backup()
    }

    val playerPush =
        PlayerPush(repositoryPlayer)

    @PostMapping("/push")
    fun push(@Valid @RequestBody updatePush: PlayerPush.UpdatePush): ResponseEntity<Any> {
        return playerPush.push(updatePush)
    }

}


















