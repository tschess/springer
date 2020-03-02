package io.bahlsenwitz.springer.controller.game

import io.bahlsenwitz.springer.controller.game.backup.GameBackUp
import io.bahlsenwitz.springer.controller.game.menu.invite.GameAck
import io.bahlsenwitz.springer.controller.game.menu.GameMenu
import io.bahlsenwitz.springer.controller.game.menu.GameRecent
import io.bahlsenwitz.springer.controller.game.menu.create.GameChallenge
import io.bahlsenwitz.springer.controller.game.tschess.update.GameCheck
import io.bahlsenwitz.springer.controller.game.tschess.resolve.GameEval
import io.bahlsenwitz.springer.controller.game.tschess.resolve.GameMate
import io.bahlsenwitz.springer.controller.game.menu.invite.GameNack
import io.bahlsenwitz.springer.controller.game.tschess.update.GameProp
import io.bahlsenwitz.springer.controller.game.menu.create.GameQuick
import io.bahlsenwitz.springer.controller.game.menu.create.GameRematch
import io.bahlsenwitz.springer.controller.game.tschess.polling.GameRequest
import io.bahlsenwitz.springer.controller.game.menu.invite.GameRescind
import io.bahlsenwitz.springer.controller.game.tschess.resolve.GameResign
import io.bahlsenwitz.springer.controller.game.tschess.GameTest
import io.bahlsenwitz.springer.controller.game.tschess.resolve.GameMine
import io.bahlsenwitz.springer.controller.game.tschess.update.GameUpdate
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

    val gameRecent = GameRecent(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

    @GetMapping("/recent/{id_player}")
    fun recent(@PathVariable(value = "id_player") id_player: String): ResponseEntity<Any> {
        return gameRecent.recent(id_player)
    }

    /**
     * Actual.swift
     */
    val gameMenu = GameMenu(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

    @PostMapping("/menu")
    fun menu(@Valid @RequestBody updateTest: GameMenu.RequestMenu): ResponseEntity<Any> {
        return gameMenu.menu(updateTest)
    }

    /**
     * Ack.swift
     */
    val gameAck = GameAck(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

    @PostMapping("/ack")
    fun ack(@Valid @RequestBody requestAck: GameAck.RequestAck): ResponseEntity<Any> {
        return gameAck.ack(requestAck)
    }

    val gameNack = GameNack(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

    @PostMapping("/nack")
    fun nack(@Valid @RequestBody updateNack: GameNack.UpdateNack): ResponseEntity<Any> {
        return gameNack.nack(updateNack)
    }

    val gameRescind = GameRescind(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

    @PostMapping("/rescind")
    fun rescind(@Valid @RequestBody updateRescind: GameRescind.UpdateRescind): ResponseEntity<Any> {
        return gameRescind.rescind(updateRescind)
    }

    /**
     * Challenge.swift
     */
    val gameChallenge = GameChallenge(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

    @PostMapping("/challenge")
    fun challenge(@Valid @RequestBody requestChallenge: GameChallenge.RequestChallenge): ResponseEntity<Any> {
        return gameChallenge.challenge(requestChallenge)
    }

    /**
     * Quick.swift
     */
    val gameQuick = GameQuick(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

    @PostMapping("/quick")
    fun quick(@Valid @RequestBody requestQuick: GameQuick.RequestQuick): ResponseEntity<Any> {
        return gameQuick.quick(requestQuick)
    }

    /**
     * Historic.swift
     */
    val gameRematch = GameRematch(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

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

    val gameResign = GameResign(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

    @PostMapping("/resign")
    fun resign(@Valid @RequestBody updateResign: GameResign.UpdateResign): ResponseEntity<Any> {
        return gameResign.resign(updateResign)
    }

    val gameUpdate = GameUpdate(repositoryGame = repositoryGame)

    @PostMapping("/update")
    fun update(@Valid @RequestBody updateGame: GameUpdate.UpdateGame): ResponseEntity<Any> {
        return gameUpdate.update(updateGame)
    }

    val gameEval = GameEval(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

    @PostMapping("/eval")
    fun eval(@Valid @RequestBody evalUpdate: GameEval.EvalUpdate): ResponseEntity<Any> {
        return gameEval.eval(evalUpdate)
    }

    val gameRequest =
        GameRequest(repositoryGame = repositoryGame)

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

    val gameMate = GameMate(
        repositoryGame = repositoryGame,
        repositoryPlayer = repositoryPlayer
    )

    @GetMapping("/mate/{id_game}")
    fun mate(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any> {
        return gameMate.mate(id_game)
    }

    val gameBackUp = GameBackUp(repositoryGame)

    @PostMapping("/backup")
    fun backup(): Any {
        return gameBackUp.backup()
    }

    val gameMine = GameMine(repositoryGame, repositoryPlayer)

    @PostMapping("/mine")
    fun mine(@Valid @RequestBody updateMine: GameMine.UpdateMine): ResponseEntity<Any> {
        return gameMine.mine(updateMine)
    }

}



























