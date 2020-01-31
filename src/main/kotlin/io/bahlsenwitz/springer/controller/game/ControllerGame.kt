package io.bahlsenwitz.springer.controller.game

import io.bahlsenwitz.springer.controller.game.actual.GameActual
import io.bahlsenwitz.springer.controller.game.challenge.GameChallenge
import io.bahlsenwitz.springer.controller.game.historic.GameHistoric
import io.bahlsenwitz.springer.controller.game.quick.GameQuick
import io.bahlsenwitz.springer.controller.game.rematch.GameRematch
import io.bahlsenwitz.springer.controller.game.snapshot.GameSnapshot
import io.bahlsenwitz.springer.controller.game.test.GameTest
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

//curl --header "Content-Type: application/json" --request POST --data '{"id":"efac3243-c71c-42f3-9f12-117cc7de6fa7", "index": 0, "size": 1}' http://localhost:8080/game/historic
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

    /**
     * EndgameSnapshot.swift
     */
    val gameSnapshot = GameSnapshot(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/snapshot")
    fun snapshot(@Valid @RequestBody requestSnapshot: GameSnapshot.RequestSnapshot): ResponseEntity<Any> {
        return gameSnapshot.snapshot(requestSnapshot)
    }

    /**
     * Challenge.swift
     */
    val gameChallenge = GameChallenge(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/challenge")
    fun challenge(@Valid @RequestBody requestChallenge: GameChallenge.RequestChallenge): ResponseEntity<Any> {
        return gameChallenge.challenge(requestChallenge)
    }

    /**
     * Quick.swift
     */
    val gameQuick = GameQuick(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/quick")
    fun quick(@Valid @RequestBody requestQuick: GameQuick.RequestQuick): ResponseEntity<Any> {
        return gameQuick.quick(requestQuick)
    }

    /**
     * Historic.swift
     */
    val gameRematch = GameRematch(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/rematch")
    fun rematch(@Valid @RequestBody requestRematch: GameRematch.RequestRematch): ResponseEntity<Any> {
        return gameRematch.rematch(requestRematch)
    }
}



























