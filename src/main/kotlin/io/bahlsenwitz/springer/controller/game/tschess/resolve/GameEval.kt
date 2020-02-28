package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.model.common.Elo
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Constant
import org.springframework.http.ResponseEntity
import java.util.*

//private val repositoryPlayer: RepositoryPlayer <-- gotta notifyu these guys...
class GameEval(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun eval(evalUpdate: EvalUpdate): ResponseEntity<Any> {
        val uuid0: UUID = UUID.fromString(evalUpdate.id_game)!!
        val game: Game = repositoryGame.findById(uuid0).get()
        game.updated = Constant().getDate()

        khttp.post(url = "${Constant().INFLUX}write?db=tschess", data = "game id=\"${game.id}\",route=\"eval\"")

        val accept: Boolean = evalUpdate.accept
        if (!accept) {
            game.condition = CONDITION.TBD
            game.turn = setTurn(turn = game.turn)
            repositoryGame.save(game)
            return ResponseEntity.ok("{\"success\": \"ok\"}")
        }

        val uuid1: UUID = UUID.fromString(evalUpdate.id_self)!!
        val playerSelf: Player = repositoryPlayer.findById(uuid1).get()
        val eloSelf0: Int = playerSelf.elo
        val eloSelf: Elo = Elo(eloSelf0)

        val uuid2: UUID = UUID.fromString(evalUpdate.id_other)!!
        val playerOther: Player = repositoryPlayer.findById(uuid2).get()
        val eloOther0: Int = playerOther.elo
        val eloOther: Elo = Elo(eloOther0)

        val eloSelf1: Int = eloSelf.update(resultActual = RESULT.DRAW, eloOpponent = eloOther0)
        playerSelf.elo = eloSelf1
        repositoryPlayer.save(playerSelf)

        val eloOther1: Int = eloOther.update(resultActual = RESULT.DRAW, eloOpponent = eloSelf0)
        playerOther.elo = eloOther1
        repositoryPlayer.save(playerOther)

        /**
         * LEADERBOARD RECALC
         */
        val playerFindAllList: List<Player> = repositoryPlayer.findAll().sorted()
        playerFindAllList.forEachIndexed forEach@{ index, player ->
            if (player.rank == index + 1) {
                player.disp = 0
                repositoryPlayer.save(player)
                return@forEach
            }
            val disp: Int = player.rank - (index + 1)
            player.disp = disp
            val date = Constant().getDate()
            player.date = date
            val rank: Int = (index + 1)
            player.rank = rank
            repositoryPlayer.save(player)
        }
        //^^^

        game.status = STATUS.RESOLVED
        game.condition = CONDITION.DRAW
        game.white_disp = repositoryPlayer.findById(uuid2).get().disp
        game.black_disp = repositoryPlayer.findById(uuid1).get().disp
        game.winner = null
        repositoryGame.save(game)
        return ResponseEntity.ok("{\"success\": \"ok\"}") //what does this need to return? the game I guess...
    }

    data class EvalUpdate(
        val id_game: String,
        val id_self: String,
        val id_other: String,
        val accept: Boolean
    )

    private fun setTurn(turn: CONTESTANT): CONTESTANT {
        if (turn == CONTESTANT.WHITE) {
            return CONTESTANT.BLACK
        }
        return CONTESTANT.WHITE
    }
}