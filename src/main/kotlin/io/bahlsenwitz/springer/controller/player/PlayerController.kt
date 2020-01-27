package io.bahlsenwitz.springer.controller.player

import io.bahlsenwitz.springer.controller.player.home.PlayerHome
import io.bahlsenwitz.springer.controller.player.initializer.PlayerInit
import io.bahlsenwitz.springer.controller.player.profile.PlayerProfile
import io.bahlsenwitz.springer.controller.player.start.PlayerStart
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/player")
class PlayerController(repositoryPlayer: RepositoryPlayer) {

    /**
     * Initializer.swift
     */
    val playerInit = PlayerInit(repositoryPlayer)

    @PostMapping("/device/{device}")
    fun device(@PathVariable(value = "device") device: String): ResponseEntity<Any> {
        return playerInit.device(device)
    }

    /**
     * Start.swift
     */
    val playerStart = PlayerStart(repositoryPlayer)

    @PostMapping("/login")
    fun login(@Valid @RequestBody requestLogin: PlayerStart.RequestLogin): ResponseEntity<Any> {
        return playerStart.login(requestLogin)
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
     * Home.swift
     */
    val playerHome = PlayerHome(repositoryPlayer)

    @PostMapping("/leaderboard/{page}")
    fun leaderboard(@PathVariable(value = "page") page: Int): ResponseEntity<Any> {
        return playerHome.leaderboard(page)
    }



}


















