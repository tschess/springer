package io.bahlsenwitz.springer.controller.player

import io.bahlsenwitz.springer.controller.player.address.PlayerAddress
import io.bahlsenwitz.springer.controller.player.config.PlayerConfig
import io.bahlsenwitz.springer.controller.player.create.PlayerCreate
import io.bahlsenwitz.springer.controller.player.home.PlayerHome
import io.bahlsenwitz.springer.controller.player.init.PlayerInit
import io.bahlsenwitz.springer.controller.player.notify.PlayerNotify
import io.bahlsenwitz.springer.controller.player.profile.PlayerProfile
import io.bahlsenwitz.springer.controller.player.quick.PlayerQuick
import io.bahlsenwitz.springer.controller.player.refresh.PlayerRefresh
import io.bahlsenwitz.springer.controller.player.skin.PlayerSkin
import io.bahlsenwitz.springer.controller.player.start.PlayerStart
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/player")
class ControllerPlayer(repositoryPlayer: RepositoryPlayer) {

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

    val playerCreate = PlayerCreate(repositoryPlayer)

    @PostMapping("/create")
    fun create(@Valid @RequestBody requestCreate: PlayerCreate.RequestCreate): ResponseEntity<Any> {
        return playerCreate.create(requestCreate)
    }

    val playerStart = PlayerStart(repositoryPlayer)

    @PostMapping("/login")
    fun login(@Valid @RequestBody requestLogin: PlayerStart.RequestLogin): ResponseEntity<Any> {
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
     * Address.swift
     */
    val playerAddress = PlayerAddress(repositoryPlayer)

    @PostMapping("/address")
    fun address(@Valid @RequestBody updateAddress: PlayerAddress.UpdateAddress): ResponseEntity<Any> {
        return playerAddress.address(updateAddress)
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

    val playerSkin = PlayerSkin(repositoryPlayer)

    @PostMapping("/skin")
    fun skin(@Valid @RequestBody updateSkin: PlayerSkin.UpdateSkin): Any {
        return playerSkin.skin(updateSkin)
    }

}


















