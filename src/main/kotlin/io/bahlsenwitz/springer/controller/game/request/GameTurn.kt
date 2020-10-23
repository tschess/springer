package io.bahlsenwitz.springer.controller.game.request

import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.push.Pusher
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime

class GameTurn(
    private val repositoryPlayer: RepositoryPlayer
) {

    private val pusher: Pusher = Pusher()
    private val dateTime: DateTime = DateTime()

    fun setTurn(game: Game): CONTESTANT {
        if (game.turn == CONTESTANT.WHITE) {
            game.white.updated = dateTime.getDate()
            /* * */
            pusher.notify(game.black)
            /* * */
            repositoryPlayer.saveAll(listOf(game.white, game.black))
            return CONTESTANT.BLACK
        }
        game.black.updated = dateTime.getDate()
        /* * */
        pusher.notify(game.white)
        /* * */
        repositoryPlayer.saveAll(listOf(game.white, game.black))
        return CONTESTANT.WHITE
    }

}