package io.bahlsenwitz.springer.controller.game.tschess

import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.push.Pusher
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.DateTime

class Tschess(
    private val repositoryPlayer: RepositoryPlayer
) {

    private val pusher: Pusher = Pusher()
    private val dateTime: DateTime = DateTime()

    fun setTurn(game: Game): CONTESTANT {
        if (game.turn == CONTESTANT.WHITE) {
            game.white.updated = dateTime.getDate()
            game.white.note_value = false
            game.black.note_value = true
            /* * */
            pusher.notify(game.black)
            /* * */
            repositoryPlayer.saveAll(listOf(game.white, game.black))
            return CONTESTANT.BLACK
        }
        game.black.updated = dateTime.getDate()
        game.black.note_value = false
        game.white.note_value = true
        /* * */
        pusher.notify(game.white)
        /* * */
        repositoryPlayer.saveAll(listOf(game.white, game.black))
        return CONTESTANT.WHITE
    }

}