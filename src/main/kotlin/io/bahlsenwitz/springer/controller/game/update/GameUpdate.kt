package io.bahlsenwitz.springer.controller.game.update

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import org.springframework.http.ResponseEntity
import java.util.*

class GameUpdate(private val repositoryGame: RepositoryGame) {

    private val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun update(updateGame: UpdateGame): ResponseEntity<Any> {
        val id: UUID = UUID.fromString(updateGame.id_game)
        val game: Game = repositoryGame.findById(id).get()
        game.state = updateGame.state
        game.moves += 1
        game.updated = DATE_TIME_GENERATOR.rightNowString()
        game.turn = setTurn(turn = game.turn)
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    private fun setTurn(turn: CONTESTANT): CONTESTANT {
        if(turn == CONTESTANT.WHITE){
            return CONTESTANT.BLACK
        }
        return CONTESTANT.WHITE
    }

    data class UpdateGame(
        val id_game: String,
        val state: List<List<String>>
    )
}