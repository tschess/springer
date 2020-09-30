package io.bahlsenwitz.springer.controller.game

import io.bahlsenwitz.springer.controller.game.path.*
import io.bahlsenwitz.springer.controller.game.path.GameProp
import io.bahlsenwitz.springer.controller.game.request.*
import io.bahlsenwitz.springer.controller.game.request.GameConfirm
import io.bahlsenwitz.springer.controller.game.request.GameUpdate
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
class GameController @Autowired
constructor(repositoryGame: RepositoryGame, repositoryPlayer: RepositoryPlayer) {

    val gameConfirm = GameConfirm(repositoryGame)
    @PostMapping("/confirm")
    fun confirm(@Valid @RequestBody updateConfirm: GameConfirm.UpdateConfirm): ResponseEntity<Any> {
        return gameConfirm.confirm(updateConfirm)
    }

    val gameRecent = GameRecent(repositoryGame, repositoryPlayer)
    @GetMapping("/recent/{id_player}")
    fun recent(@PathVariable(value = "id_player") id_player: String): ResponseEntity<Any> {
        return gameRecent.recent(id_player)
    }

    /**
     * Actual.swift
     */
    val gameMenu = GameMenu(repositoryGame, repositoryPlayer)
    @PostMapping("/menu")
    fun menu(@Valid @RequestBody updateTest: GameMenu.RequestMenu): ResponseEntity<Any> {
        return gameMenu.menu(updateTest)
    }

    /**
     * Ack.swift
     */
    val gameAck = GameAck(repositoryGame, repositoryPlayer)
    @PostMapping("/ack")
    fun ack(@Valid @RequestBody requestAck: GameAck.RequestAck): ResponseEntity<Any> {
        return gameAck.ack(requestAck)
    }

    val gameNack = GameNack(repositoryGame, repositoryPlayer)
    @PostMapping("/nack")
    fun nack(@Valid @RequestBody updateNack: UpdateNack): ResponseEntity<Any> {
        return gameNack.nack(updateNack)
    }

    val gameRescind = GameRescind(repositoryGame, repositoryPlayer)
    @PostMapping("/rescind")
    fun rescind(@Valid @RequestBody updateRescind: UpdateNack): ResponseEntity<Any> {
        return gameRescind.rescind(updateRescind)
    }

    /**
     * Challenge.swift
     */
    val gameChallenge = GameChallenge(repositoryGame, repositoryPlayer)
    @PostMapping("/challenge")
    fun challenge(@Valid @RequestBody requestChallenge: RequestCreate): ResponseEntity<Any> {
        return gameChallenge.challenge(requestChallenge)
    }

    /**
     * Historic.swift
     */
    val gameRematch = GameRematch(repositoryGame, repositoryPlayer)
    @PostMapping("/rematch")
    fun rematch(@Valid @RequestBody requestRematch: GameRematch.RequestRematch): ResponseEntity<Any> {
        return gameRematch.rematch(requestRematch)
    }

    /**
     * Tschess.swift
     */

    val gameTimeout: GameTimeout = GameTimeout(repositoryGame, repositoryPlayer)
    @GetMapping("/timeout/{id_game}")
    fun timeout(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any>? {
        return gameTimeout.timeout(id_game)
    }

    val gameResign: GameResign = GameResign(repositoryGame, repositoryPlayer)
    @PostMapping("/resign")
    fun resign(@Valid @RequestBody updateResign: GameResign.UpdateResign): ResponseEntity<Any>? {
        return gameResign.resign(updateResign)
    }

    val gameUpdate = GameUpdate(repositoryGame, repositoryPlayer)
    @PostMapping("/update")
    fun update(@Valid @RequestBody updateGame: GameUpdate.UpdateGame): ResponseEntity<Any>? {
        return gameUpdate.update(updateGame)
    }

    val gameEval = GameEval(repositoryGame, repositoryPlayer)
    @PostMapping("/eval")
    fun eval(@Valid @RequestBody evalUpdate: GameEval.EvalUpdate): ResponseEntity<Any> {
        return gameEval.eval(evalUpdate)
    }

    val gameRequest = GameRequest(repositoryGame)
    @GetMapping("/request/{id_game}")
    fun request(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any> {
        return gameRequest.request(id_game)
    }

    val gameProp = GameProp(repositoryGame, repositoryPlayer)
    @GetMapping("/prop/{id_game}")
    fun prop(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any> {
        return gameProp.prop(id_game)
    }

    val gameCheck = GameCheck(repositoryGame)
    @GetMapping("/check/{id_game}")
    fun check(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any>? {
        return gameCheck.check(id_game)
    }

    val gameMate = GameMate(repositoryGame, repositoryPlayer)
    @GetMapping("/mate/{id_game}")
    fun mate(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any>?{
        return gameMate.mate(id_game)
    }

    val gameBackUp = GameBackUp(repositoryGame)
    @PostMapping("/backup")
    fun backup(): Any {
        return gameBackUp.backup()
    }

    val gameMine = GameMine(repositoryGame, repositoryPlayer)
    @PostMapping("/mine")
    fun mine(@Valid @RequestBody updateMine: GameMine.UpdateMine): ResponseEntity<Any>? {
        return gameMine.mine(updateMine)
    }

}



























