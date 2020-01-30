package io.bahlsenwitz.springer.generator.test

import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.repository.RepositoryGame

class GeneratorTestGamePro(
    private val repositoryGame: RepositoryGame,
    private val generatorTestPlayer: GeneratorTestPlayer
) {

    private val DATE_TIME_GENERATOR = GeneratorDateTime()

    fun generate() {

        val invite00 = Game(
            white = generatorTestPlayer.findByName(username = "777"),
            black = generatorTestPlayer.findByName(username = "aaa"),
            status = STATUS.PROPOSED,
            challenger = CONTESTANT.BLACK,
            date_create = DATE_TIME_GENERATOR.rightNowString()
        )
        repositoryGame.save(invite00)

        val invite01 = Game(
            white = generatorTestPlayer.findByName(username = "aaa"),
            black = generatorTestPlayer.findByName(username = "ccc"),
            status = STATUS.PROPOSED,
            challenger = CONTESTANT.BLACK,
            date_create = DATE_TIME_GENERATOR.rightNowString()
        )
        repositoryGame.save(invite01)

        val invite02 = Game(
            white = generatorTestPlayer.findByName(username = "aaa"),
            black = generatorTestPlayer.findByName(username = "888"),
            status = STATUS.ONGOING,
            challenger = CONTESTANT.BLACK,
            date_create = DATE_TIME_GENERATOR.rightNowString(),
            date_update = DATE_TIME_GENERATOR.rightNowString(),
            turn = CONTESTANT.BLACK
        )
        repositoryGame.save(invite02)

        val invite03 = Game(
            white = generatorTestPlayer.findByName(username = "999"),
            black = generatorTestPlayer.findByName(username = "aaa"),
            status = STATUS.ONGOING,
            challenger = CONTESTANT.BLACK,
            date_create = DATE_TIME_GENERATOR.rightNowString(),
            date_update = DATE_TIME_GENERATOR.rightNowString(),
            turn = CONTESTANT.BLACK
        )
        repositoryGame.save(invite03)
    }
}