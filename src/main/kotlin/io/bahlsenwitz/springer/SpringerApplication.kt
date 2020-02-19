package io.bahlsenwitz.springer

import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import io.bahlsenwitz.springer.generator.backup.*
//import io.bahlsenwitz.springer.generator.test.*

@SpringBootApplication
class SpringerApplication(
    val repositoryPlayer: RepositoryPlayer,
    val repositoryGame: RepositoryGame): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        //val generatorTestPlayer = GeneratorTestPlayer(repositoryPlayer)
        //generatorTestPlayer.generate()
        //Thread.sleep(1_000)
        //GeneratorTestGameFin(repositoryGame, generatorTestPlayer).generate()
        //Thread.sleep(1_000)
        //GeneratorTestGameAct(repositoryGame, generatorTestPlayer).generate()
        //Thread.sleep(1_000)
        //GeneratorTestGamePro(repositoryGame, generatorTestPlayer).generate()

        val generatorPlayer = GeneratorPlayer(repositoryPlayer)
        generatorPlayer.generate()
        Thread.sleep(1_000)
        val generatorGame = GeneratorGame(repositoryPlayer, repositoryGame)
        generatorGame.generate()
    }
}

fun main(args: Array<String>) {
    runApplication<SpringerApplication>(*args)
}
