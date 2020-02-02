package io.bahlsenwitz.springer.generator.test

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.repository.RepositoryGame
import java.util.*

class GeneratorTestGameAct(
    private val repositoryGame: RepositoryGame,
    private val generatorTestPlayer: GeneratorTestPlayer
) {

    fun generate() {
        val test = Game(
            id_game = UUID.fromString("00000000-0000-0000-0000-000000000000")!!,
            white = generatorTestPlayer.findByName(username = "white"),
            black = generatorTestPlayer.findByName(username = "black")
        )
        repositoryGame.save(test)
    }
}