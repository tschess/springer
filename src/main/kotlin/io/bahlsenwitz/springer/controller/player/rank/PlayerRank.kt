package io.bahlsenwitz.springer.controller.player.rank

import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer

class PlayerRank(private val repositoryPlayer: RepositoryPlayer) {

    fun order() {

        val playerAll: List<Player> = repositoryPlayer.findAll().sorted()



    }

}