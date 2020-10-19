package io.bahlsenwitz.springer.controller.game

import io.bahlsenwitz.springer.controller.game.backup.GameBackUp
import io.bahlsenwitz.springer.controller.game.path.*
import io.bahlsenwitz.springer.controller.game.path.GameProp
import io.bahlsenwitz.springer.controller.game.request.*
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
constructor(val repositoryGame: RepositoryGame, val repositoryPlayer: RepositoryPlayer) {

    @GetMapping("/recent/{id_player}")
    fun recent(@PathVariable(value = "id_player") id_player: String): ResponseEntity<Any> {
        return GameRecent(repositoryGame, repositoryPlayer).recent(id_player)
    }

    @GetMapping("/timeout/{id_game}")
    fun timeout(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any>? {
        return GameTimeout(repositoryGame, repositoryPlayer).timeout(id_game)
    }

    @GetMapping("/request/{id_game}")
    fun request(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any> {
        return GameRequest(repositoryGame).request(id_game)
    }

    @GetMapping("/prop/{id_game}")
    fun prop(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any> {
        return GameProp(repositoryGame, repositoryPlayer).prop(id_game)
    }

    @GetMapping("/check/{id_game}")
    fun check(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any>? {
        return GameCheck(repositoryGame).check(id_game)
    }

    @GetMapping("/mate/{id_game}")
    fun mate(@PathVariable(value = "id_game") id_game: String): ResponseEntity<Any>?{
        return GameMate(repositoryGame, repositoryPlayer).mate(id_game)
    }

    @PostMapping("/mine")
    fun mine(@Valid @RequestBody updateMine: GameMine.UpdateMine): ResponseEntity<Any>? {
        return GameMine(repositoryGame, repositoryPlayer).mine(updateMine)
    }

    @PostMapping("/update")
    fun update(@Valid @RequestBody updateGame: GameUpdate.UpdateGame): ResponseEntity<Any>? {
        return GameUpdate(repositoryGame, repositoryPlayer).update(updateGame)
    }

    @PostMapping("/eval")
    fun eval(@Valid @RequestBody evalUpdate: GameEval.EvalUpdate): ResponseEntity<Any> {
        return GameEval(repositoryGame, repositoryPlayer).eval(evalUpdate)
    }

    @PostMapping("/resign")
    fun resign(@Valid @RequestBody updateResign: GameResign.UpdateResign): ResponseEntity<Any>? {
        return GameResign(repositoryGame, repositoryPlayer).resign(updateResign)
    }

    @PostMapping("/rematch")
    fun rematch(@Valid @RequestBody requestRematch: GameRematch.RequestRematch): ResponseEntity<Any> {
        return GameRematch(repositoryGame, repositoryPlayer).rematch(requestRematch)
    }

    @PostMapping("/challenge")
    fun challenge(@Valid @RequestBody requestChallenge: RequestCreate): ResponseEntity<Any> {
        return GameChallenge(repositoryGame, repositoryPlayer).challenge(requestChallenge)
    }

    @PostMapping("/menu")
    fun menu(@Valid @RequestBody updateTest: GameMenu.RequestMenu): ResponseEntity<Any> {
        return GameMenu(repositoryGame, repositoryPlayer).menu(updateTest)
    }

    @PostMapping("/ack")
    fun ack(@Valid @RequestBody requestAck: GameAck.RequestAck): ResponseEntity<Any> {
        return GameAck(repositoryGame, repositoryPlayer).ack(requestAck)
    }

    @PostMapping("/nack")
    fun nack(@Valid @RequestBody updateNack: UpdateNack): ResponseEntity<Any> {
        return GameNack(repositoryGame, repositoryPlayer).nack(updateNack)
    }

    @PostMapping("/rescind")
    fun rescind(@Valid @RequestBody updateRescind: UpdateNack): ResponseEntity<Any> {
        return GameRescind(repositoryGame, repositoryPlayer).rescind(updateRescind)
    }

    @PostMapping("/backup")
    fun backup(): Any {
        return GameBackUp(repositoryGame).backup()
    }

}