package io.bahlsenwitz.springer.generator.test

import io.bahlsenwitz.springer.generator.common.Default
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame
import java.util.*

class GeneratorTestGameAct(
    private val repositoryGame: RepositoryGame,
    private val generatorTestPlayer: GeneratorTestPlayer
) {

    fun generate() {
        val test = Game(
            id = UUID.fromString("00000000-0000-0000-0000-000000000000")!!,
            white = generatorTestPlayer.findByName(username = "white"),
            black = generatorTestPlayer.findByName(username = "black"),
            state = Default.state(),
            challenger = CONTESTANT.WHITE,
            status = STATUS.ONGOING
        )
        repositoryGame.save(test)
    }
}