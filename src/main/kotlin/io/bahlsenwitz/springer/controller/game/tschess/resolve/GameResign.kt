package io.bahlsenwitz.springer.controller.game.tschess.resolve

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.common.Elo
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.OUTCOME
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.ResponseEntity
import java.util.*

class GameResign(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {
    val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun resign(updateResign: UpdateResign): ResponseEntity<Any> {
        val uuid0: UUID = UUID.fromString(updateResign.id_game)!!
        val uuid1: UUID = UUID.fromString(updateResign.id_self)!!
        val uuid2: UUID = UUID.fromString(updateResign.id_oppo)!!

        val playerSelf: Player = repositoryPlayer.findById(uuid1).get()
        val eloSelf0: Int = playerSelf.elo
        val eloSelf: Elo = Elo(eloSelf0)

        val playerOppo: Player = repositoryPlayer.findById(uuid2).get()
        val eloOppo0: Int = playerOppo.elo
        val eloOppo: Elo = Elo(eloOppo0)

        val eloSelf1: Int = eloSelf.update(resultActual = RESULT.LOSS, eloOpponent = eloOppo0)
        playerSelf.elo = eloSelf1
        repositoryPlayer.save(playerSelf)

        val eloOppo1: Int = eloOppo.update(resultActual = RESULT.WIN, eloOpponent = eloSelf0)
        playerOppo.elo = eloOppo1
        repositoryPlayer.save(playerOppo)

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
            val date: String = DATE_TIME_GENERATOR.rightNowString()
            player.date = date
            val rank: Int = (index + 1)
            player.rank = rank
            repositoryPlayer.save(player)
        }
        //^^^

        val game: Game = repositoryGame.findById(uuid0).get()
        game.status = STATUS.RESOLVED
        game.outcome = OUTCOME.RESIGN
        if(updateResign.white){
            game.white_disp = repositoryPlayer.findById(uuid1).get().disp
            game.black_disp = repositoryPlayer.findById(uuid2).get().disp
            game.winner = CONTESTANT.BLACK
        } else {
            game.white_disp = repositoryPlayer.findById(uuid2).get().disp
            game.black_disp = repositoryPlayer.findById(uuid1).get().disp
            game.winner = CONTESTANT.WHITE
        }
        game.highlight = "TBD"
        game.updated = DATE_TIME_GENERATOR.rightNowString()
        repositoryGame.save(game)
        return ResponseEntity.ok("{\"success\": \"ok\"}") //what does this need to return? the game I guess...
    }

    data class UpdateResign(
        val id_game: String,
        val id_self: String,
        val id_oppo: String,
        val white: Boolean
    )
}