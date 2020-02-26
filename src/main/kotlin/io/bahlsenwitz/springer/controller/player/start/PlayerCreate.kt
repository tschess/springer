package io.bahlsenwitz.springer.controller.player.start

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Constant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid

class PlayerCreate(private val repositoryPlayer: RepositoryPlayer) {

    fun create(@Valid @RequestBody requestCreate: RequestCreate): ResponseEntity<Any> {

        if (repositoryPlayer.findByUsername(requestCreate.username) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"reserved\"}")
        }

        val player = Player(
            username = requestCreate.username,
            password = BCryptPasswordEncoder().encode(requestCreate.password),
            device = requestCreate.device
        )
        repositoryPlayer.save(player)

        khttp.post(url = "${Constant().INFLUX}write?db=tschess", data = "growth player=\"${player.id}\"")

        /**
         * LEADERBOARD RECALC
         */
        val playerFindAllList: List<Player> = repositoryPlayer.findAll().sorted()
        playerFindAllList.forEachIndexed forEach@{ index, playerX ->
            if (playerX.rank == index + 1) {
                playerX.disp = 0
                repositoryPlayer.save(playerX)
                return@forEach
            }
            val disp: Int = playerX.rank - (index + 1)
            playerX.disp = disp
            val date = Constant().getDate()
            playerX.date = date
            val rank: Int = (index + 1)
            playerX.rank = rank
            repositoryPlayer.save(playerX)
        }
        //^^^
        player.disp = 0 //moral purposes...

        return ResponseEntity.ok(repositoryPlayer.save(player))
    }

    data class RequestCreate(
        val username: String,
        val password: String,
        val device: String
    )
}