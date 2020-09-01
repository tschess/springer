package io.bahlsenwitz.springer.controller.game.tschess.update

import io.bahlsenwitz.springer.controller.Output
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import org.springframework.http.ResponseEntity
import java.util.*

class GameConfirm(private val repositoryGame: RepositoryGame) {

    private val output: Output = Output(repositoryGame = repositoryGame)

    data class UpdateConfirm(
        val id_game: String,
        val white: Boolean
    )

    fun confirm(updateConfirm: UpdateConfirm): ResponseEntity<Any> {
        val game: Game = repositoryGame.findById(UUID.fromString(updateConfirm.id_game)!!).get()
        if(game.status == STATUS.RESOLVED){
            return output.terminal(result = "fail", route = "confirm")
        }
        if(game.status == STATUS.RESOLVED_WHITE_BLACK){
            if(updateConfirm.white){
                game.status = STATUS.RESOLVED_BLACK
                return output.update(route = "confirm", game = game)
            }
            game.status = STATUS.RESOLVED_WHITE
            return output.update(route = "confirm", game = game)
        }
        game.status = STATUS.RESOLVED
        return output.update(route = "confirm", game = game)
    }
}