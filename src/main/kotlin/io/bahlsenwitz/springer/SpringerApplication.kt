package io.bahlsenwitz.springer

import io.bahlsenwitz.springer.generator.test.*
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringerApplication(
    val repositoryPlayer: RepositoryPlayer,
    val repositoryGame: RepositoryGame): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {

        val generatorTestPlayer = GeneratorTestPlayer(repositoryPlayer)
        generatorTestPlayer.generate()

        Thread.sleep(1_000)

        GeneratorTestGameFin(repositoryGame, generatorTestPlayer).generate()

        Thread.sleep(1_000)

        GeneratorTestGameAct(repositoryGame, generatorTestPlayer).generate()

        Thread.sleep(1_000)

        GeneratorTestGamePro(repositoryGame, generatorTestPlayer).generate()
    }
}

fun main(args: Array<String>) {
    runApplication<SpringerApplication>(*args)
}
