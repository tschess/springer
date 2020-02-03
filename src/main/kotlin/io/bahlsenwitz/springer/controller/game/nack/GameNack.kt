package io.bahlsenwitz.springer.controller.game.nack

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.common.Elo
import io.bahlsenwitz.springer.model.common.RESULT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

class GameNack(
    private val repositoryGame: RepositoryGame,
    private val repositoryPlayer: RepositoryPlayer
) {
    val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun nack(updateNack: UpdateNack): ResponseEntity<Any> {
        val uuid0: UUID = UUID.fromString(updateNack.id_game)!!
        val game: Game = repositoryGame.findById(uuid0).get()
        game.status = STATUS.DECLINED
        repositoryGame.save(game)

        val uuid1: UUID = UUID.fromString(updateNack.id_player)!!
        val player0: Player = repositoryPlayer.findById(uuid1).get()

        val elo0: Int = player0.elo
        val elo: Elo = Elo(elo0)
        val elo1: Int = elo.update(resultActual = RESULT.LOSS, eloOpponent = elo0)

        player0.elo = elo1
        repositoryPlayer.save(player0)

        /**
         * LEADERBOARD RECALC
         */
        val playerFindAllList: List<Player> = repositoryPlayer.findAll().sorted()
        playerFindAllList.forEachIndexed forEach@{ index, player ->
            if(player.rank == index){
                return@forEach
            }
            val disp: Int = player.rank - index
            player.disp = disp
            val date: String = DATE_TIME_GENERATOR.rightNowString()
            player.date = date
            val rank: Int = index
            player.rank = rank
            repositoryPlayer.save(player)
        }
        //^^^
        
        return ResponseEntity.status(HttpStatus.OK).body("{\"nack\": \"${game.id}\"}")
    }

    data class UpdateNack(
        val id_game: String,
        val id_player: String
    )
}