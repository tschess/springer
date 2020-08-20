package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer

class Churn(private val repositoryPlayer: RepositoryPlayer) {

    fun calculate(player: Player): Player {
        val listHead: List<Player> = repositoryPlayer.findAll().filter { it.id != player.id }.sorted().take(11)
        return listHead.shuffled().take(1)[0]
    }
}