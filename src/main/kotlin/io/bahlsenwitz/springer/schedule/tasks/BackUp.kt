package io.bahlsenwitz.springer.schedule.tasks

import io.bahlsenwitz.springer.controller.game.GameBackUp
import io.bahlsenwitz.springer.controller.player.PlayerBackUp
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer

class BackUp(val repositoryPlayer: RepositoryPlayer, val repositoryGame: RepositoryGame) {

    fun execute() {
        PlayerBackUp(repositoryPlayer).backup()
        GameBackUp(repositoryGame).backup()
    }

}