package io.bahlsenwitz.springer.generator.test

import io.bahlsenwitz.springer.generator.common.Default
import io.bahlsenwitz.springer.model.game.*
import io.bahlsenwitz.springer.repository.RepositoryGame
import java.util.*

class GeneratorTestGameFin(
    private val repositoryGame: RepositoryGame,
    private val generatorTestPlayer: GeneratorTestPlayer
) {

    fun generate() {
        repositoryGame.deleteAll()

        val snap = Game(
            id = UUID.fromString("11111111-1111-1111-1111-111111111111")!!,
            white = generatorTestPlayer.findByName(username = "999"),
            black = generatorTestPlayer.findByName(username = "888"),
            state = Default.state(),
            moves = 33,

            condition = CONDITION.CHECKMATE,
            winner = CONTESTANT.WHITE,
            status = STATUS.RESOLVED,
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(snap)

        val game00 = Game(
            white = generatorTestPlayer.findByName(username = "sme"),
            black = generatorTestPlayer.findByName(username = "bbb"),
            status = STATUS.RESOLVED,
            winner = CONTESTANT.WHITE,

            state = Default.state(),
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(game00)

        Thread.sleep(1_000)

        val game01 = Game(
            white = generatorTestPlayer.findByName(username = "sme"),
            black = generatorTestPlayer.findByName(username = "ccc"),
            status = STATUS.RESOLVED,
            winner = CONTESTANT.WHITE,


            state = Default.state(),
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(game01)

        Thread.sleep(1_000)

        val game02 = Game(
            white = generatorTestPlayer.findByName(username = "sme"),
            black = generatorTestPlayer.findByName(username = "ddd"),
            status = STATUS.RESOLVED,
            winner = CONTESTANT.BLACK,

            state = Default.state(),
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(game02)

        Thread.sleep(1_000)

        val game03 = Game(
            white = generatorTestPlayer.findByName(username = "sme"),
            black = generatorTestPlayer.findByName(username = "eee"),
            status = STATUS.RESOLVED,


            state = Default.state(),
            challenger = CONTESTANT.WHITE,
            winner = CONTESTANT.WHITE
        )
        repositoryGame.save(game03)

        val game04 = Game(
            white = generatorTestPlayer.findByName(username = "sme"),
            black = generatorTestPlayer.findByName(username = "fff"),
            status = STATUS.RESOLVED,
            winner = CONTESTANT.BLACK,


            state = Default.state(),
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(game04)
    }
}