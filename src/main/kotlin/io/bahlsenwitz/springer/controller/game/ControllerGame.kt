package io.bahlsenwitz.springer.controller.game

import io.bahlsenwitz.springer.controller.game.actual.GameActual
import io.bahlsenwitz.springer.controller.game.historic.GameHistoric
import io.bahlsenwitz.springer.controller.game.test.GameTest
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

//curl --header "Content-Type: application/json" --request POST --data '{"id":"97b8d685-7745-49a6-a858-b403b2d2f059", "index": 0, "size": 1}' http://3.12.121.89:8080/game/actual

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

    /**
     * Actual.swift
     */
    val gameActual = GameActual(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/actual")
    fun actual(@Valid @RequestBody updateTest: GameActual.RequestActual): ResponseEntity<Any> {
        return gameActual.actual(updateTest)
    }

    /**
     * Other.swift & Historic.swift
     */
    val gameHistoric = GameHistoric(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/historic")
    fun historic(@Valid @RequestBody updateTest: GameHistoric.RequestHistoric): ResponseEntity<Any> {
        return gameHistoric.historic(updateTest)
    }
}