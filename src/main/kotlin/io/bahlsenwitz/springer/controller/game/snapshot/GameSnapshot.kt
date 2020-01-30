package io.bahlsenwitz.springer.controller.game.snapshot

import io.bahlsenwitz.springer.controller.game.historic.GameHistoric
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class GameSnapshot(private val repositoryGame: RepositoryGame,
                   private val repositoryPlayer: RepositoryPlayer
) {

    //val game_id: String = game.id.toString()
    //val date_end: String = game.date_end
    //val opponent_id: String = info.id
    //val opponent_avatar: String = info.avatar
    //val opponent_username: String = info.username
    //val disp: Int = info.disp
    //val odds: Int = info.odds
    //val winner: Int = GameCoreHistoric.getWinner(player, game)

    fun snapshot(requestHistoric: GameHistoric.RequestHistoric): ResponseEntity<Any> {

        val uuid: UUID = UUID.fromString(requestHistoric.id)!!
        val game: Game = repositoryGame.findById(uuid).get()

    }
}