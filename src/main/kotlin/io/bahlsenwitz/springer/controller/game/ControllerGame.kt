package io.bahlsenwitz.springer.controller.game

import io.bahlsenwitz.springer.controller.game.test.GameTest
import io.bahlsenwitz.springer.model.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@SpringBootApplication(scanBasePackages = ["io.bahlsenwitz.springer"])
@RestController
@RequestMapping("/game")
class ControllerGame @Autowired
constructor(repositoryGame: RepositoryGame, repositoryPlayer: RepositoryPlayer) {

    /**
     * Start.swift
     */
    val gameTest = GameTest(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/test/update")
    fun updateTest(@Valid @RequestBody updateTest: GameTest.UpdateTest): ResponseEntity<Any> {
        return gameTest.updateTest(updateTest)
    }

    @GetMapping("/test/request")
    fun requestTest(@Valid @RequestBody requestTest: GameTest.RequestTest): ResponseEntity<Any> {
        return gameTest.requestTest(requestTest)
    }
}