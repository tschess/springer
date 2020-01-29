package io.bahlsenwitz.springer.controller.game

import io.bahlsenwitz.springer.controller.game.historic.GameHistoric
import io.bahlsenwitz.springer.controller.game.test.GameTest
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

//curl --header "Content-Type: application/json" --request POST --data '{"id":"01b43af0-58a9-4dae-8f00-1637658af53c", "index": 0, "size": 1}' http://3.12.121.89:8080/game/other

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
     * Other.swift
     * &
     * Historic.swift
     */
    val gameHistoric = GameHistoric(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

    @PostMapping("/historic")
    fun historic(@Valid @RequestBody updateTest: GameHistoric.RequestHistoric): ResponseEntity<Any> {
        return gameHistoric.historic(updateTest)
    }
}