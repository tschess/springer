package io.bahlsenwitz.springer

import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.*
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringerApplication(
    val repositoryPlayer: RepositoryPlayer,
    val repositoryGame: RepositoryGame): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {

        val dataGeneratorToy = DataGeneratorToy(repositoryPlayer = repositoryPlayer)
        dataGeneratorToy.defaultData()
    }
}

fun main(args: Array<String>) {
    runApplication<SpringerApplication>(*args)
}
