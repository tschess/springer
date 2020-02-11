package io.bahlsenwitz.springer.controller.player.create

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid

class PlayerCreate(private val repositoryPlayer: RepositoryPlayer) {

    private val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun create(@Valid @RequestBody requestCreate: RequestCreate): ResponseEntity<Any> {

        if (repositoryPlayer.findByUsername(requestCreate.username) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"reserved\"}")
        }

        val player = Player(
            username = requestCreate.username,
            password = BCryptPasswordEncoder().encode(requestCreate.password),
            date = DATE_TIME_GENERATOR.rightNowString(),
            device = requestCreate.device
        )
        repositoryPlayer.save(player)

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

        return ResponseEntity.ok(player)
    }

    data class RequestCreate(
        val username: String,
        val password: String,
        val device: String
    )
}