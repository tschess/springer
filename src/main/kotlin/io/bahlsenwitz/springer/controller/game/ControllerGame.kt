package io.bahlsenwitz.springer.controller.game

import io.bahlsenwitz.springer.controller.game.ack.GameAck
import io.bahlsenwitz.springer.controller.game.actual.GameActual
import io.bahlsenwitz.springer.controller.game.challenge.GameChallenge
import io.bahlsenwitz.springer.controller.game.check.GameCheck
import io.bahlsenwitz.springer.controller.game.eval.GameEval
import io.bahlsenwitz.springer.controller.game.historic.GameHistoric
import io.bahlsenwitz.springer.controller.game.mate.GameMate
import io.bahlsenwitz.springer.controller.game.nack.GameNack
import io.bahlsenwitz.springer.controller.game.prop.GameProp
import io.bahlsenwitz.springer.controller.game.quick.GameQuick
import io.bahlsenwitz.springer.controller.game.rematch.GameRematch
import io.bahlsenwitz.springer.controller.game.request.GameRequest
import io.bahlsenwitz.springer.controller.game.rescind.GameRescind
import io.bahlsenwitz.springer.controller.game.resign.GameResign
import io.bahlsenwitz.springer.controller.game.snapshot.GameSnapshot
import io.bahlsenwitz.springer.controller.game.test.GameTest
import io.bahlsenwitz.springer.controller.game.update.GameUpdate
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
     * Actual.swift
     */
    val gameActual = GameActual(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/actual")
    fun actual(@Valid @RequestBody updateTest: GameActual.RequestActual): ResponseEntity<Any> {
        return gameActual.actual(updateTest)
    }

    /**
     * Ack.swift
     */
    val gameAck = GameAck(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/ack")
    fun ack(@Valid @RequestBody requestAck: GameAck.RequestAck): ResponseEntity<Any> {
        return gameAck.ack(requestAck)
    }

    val gameNack = GameNack(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/nack")
    fun nack(@Valid @RequestBody updateNack: GameNack.UpdateNack): ResponseEntity<Any> {
        return gameNack.nack(updateNack)
    }

    val gameRescind = GameRescind(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/rescind")
    fun rescind(@Valid @RequestBody updateRescind: GameRescind.UpdateRescind): ResponseEntity<Any> {
        return gameRescind.rescind(updateRescind)
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

    /**
     * Start.swift
     */
    val gameTest = GameTest(repositoryGame = repositoryGame)

    @PostMapping("/test")
    fun test(@Valid @RequestBody requestTest: GameTest.RequestTest): ResponseEntity<Any> {
        return gameTest.test(requestTest)
    }

    /**
     * Tschess.swift
     */

    val gameResign = GameResign(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/resign")
    fun resign(@Valid @RequestBody updateResign: GameResign.UpdateResign): ResponseEntity<Any> {
        return gameResign.resign(updateResign)
    }

    val gameUpdate = GameUpdate(repositoryGame = repositoryGame)

    @PostMapping("/update")
    fun update(@Valid @RequestBody updateGame: GameUpdate.UpdateGame): ResponseEntity<Any> {
        return gameUpdate.update(updateGame)
    }

    val gameEval = GameEval(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @PostMapping("/eval")
    fun eval(@Valid @RequestBody evalUpdate: GameEval.EvalUpdate): ResponseEntity<Any> {
        return gameEval.eval(evalUpdate)
    }

    val gameRequest = GameRequest(repositoryGame = repositoryGame)

    @GetMapping("/request/{id_game}")
    fun request(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any> {
        return gameRequest.request(id_game)
    }

    val gameProp = GameProp(repositoryGame = repositoryGame)

    @GetMapping("/prop/{id_game}")
    fun prop(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any> {
        return gameProp.prop(id_game)
    }

    val gameCheck = GameCheck(repositoryGame = repositoryGame)

    @GetMapping("/check/{id_game}")
    fun check(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any> {
        return gameCheck.check(id_game)
    }

    val gameMate = GameMate(repositoryGame = repositoryGame, repositoryPlayer = repositoryPlayer)

    @GetMapping("/mate/{id_game}")
    fun mate(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any> {
        return gameMate.mate(id_game)
    }
}



























