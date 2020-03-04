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
            white_disp = 1,
            white_skin = SKIN.DEFAULT,
            black_skin = SKIN.DEFAULT,
            black_disp = -7,
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
            white_disp = 1,
            white_skin = SKIN.DEFAULT,
            black_skin = SKIN.DEFAULT,
            black_disp = -7,
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
            white_disp = -3,
            black_disp = 1,
            white_skin = SKIN.DEFAULT,
            black_skin = SKIN.DEFAULT,
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
            white_disp = -9,
            black_disp = 0,
            state = Default.state(),
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(game02)

        Thread.sleep(1_000)

        val game03 = Game(
            white = generatorTestPlayer.findByName(username = "sme"),
            black = generatorTestPlayer.findByName(username = "eee"),
            status = STATUS.RESOLVED,
            white_disp = 14,
            black_disp = -4,
            white_skin = SKIN.DEFAULT,
            black_skin = SKIN.DEFAULT,
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
            white_disp = 6,
            black_disp = -2,
            white_skin = SKIN.DEFAULT,
            black_skin = SKIN.DEFAULT,
            state = Default.state(),
            challenger = CONTESTANT.WHITE
        )
        repositoryGame.save(game04)
    }
}