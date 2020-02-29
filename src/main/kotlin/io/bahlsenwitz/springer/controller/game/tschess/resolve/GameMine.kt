package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.model.common.Elo
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONDITION
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Constant
import org.springframework.http.ResponseEntity
import java.util.*

class GameMine(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {

    fun mine(updateMine: UpdateMine): ResponseEntity<Any> {
        val uuid: UUID = UUID.fromString(updateMine.id_game)!!
        val game: Game = repositoryGame.findById(uuid).get()

        khttp.post(url = "${Constant().INFLUX}write?db=tschess", data = "game id=\"${game.id}\",route=\"mine\"")

        game.state = updateMine.state
        game.status = STATUS.RESOLVED
        game.condition = CONDITION.LANDMINE //!!!
        /***/
        if (game.turn == CONTESTANT.WHITE) {
            game.winner = CONTESTANT.BLACK

            val wnElo0: Int = game.black.elo
            val wnElo: Elo = Elo(wnElo0)

            val lsElo0: Int = game.white.elo
            val lsElo: Elo = Elo(lsElo0)

            val wnElo1: Int = wnElo.update(resultActual = RESULT.WIN, eloOpponent = lsElo0)
            val lsElo1: Int = lsElo.update(resultActual = RESULT.LOSS, eloOpponent = wnElo0)

            game.black.elo = wnElo1
            game.white.elo = lsElo1
        } else {
            game.winner = CONTESTANT.WHITE

            val wnElo0: Int = game.white.elo
            val wnElo: Elo = Elo(wnElo0)

            val lsElo0: Int = game.black.elo
            val lsElo: Elo = Elo(lsElo0)

            val wnElo1: Int = wnElo.update(resultActual = RESULT.WIN, eloOpponent = lsElo0)
            val lsElo1: Int = lsElo.update(resultActual = RESULT.LOSS, eloOpponent = wnElo0)

            game.black.elo = lsElo1
            game.white.elo = wnElo1
        }
        repositoryPlayer.save(game.black)
        repositoryPlayer.save(game.white)

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

        game.white_disp = repositoryPlayer.findById(game.white.id).get().disp
        game.black_disp = repositoryPlayer.findById(game.black.id).get().disp
        game.highlight = "TBD"
        game.updated = Constant().getDate()
        repositoryGame.save(game)
        return ResponseEntity.ok("{\"success\": \"ok\"}")
    }

    data class UpdateMine(  //you can change them to REVEAL only on the server./..
        val id_game: String,
        val state: List<List<String>>
    )
}