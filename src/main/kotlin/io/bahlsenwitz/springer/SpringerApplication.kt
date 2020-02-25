package io.bahlsenwitz.springer

import io.bahlsenwitz.springer.generator.test.GeneratorTestGameAct
import io.bahlsenwitz.springer.generator.test.GeneratorTestGameFin
import io.bahlsenwitz.springer.generator.test.GeneratorTestGamePro
import io.bahlsenwitz.springer.generator.test.GeneratorTestPlayer
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

    //./gradlew bootRun --args='--source=date'
    override fun run(args: ApplicationArguments?) {

        if(args == null){ // thow an error actally...
            val generatorTestPlayer = GeneratorTestPlayer(repositoryPlayer)
            generatorTestPlayer.generate()
            Thread.sleep(1_000)
            GeneratorTestGameFin(repositoryGame, generatorTestPlayer).generate()
            Thread.sleep(1_000)
            GeneratorTestGameAct(repositoryGame, generatorTestPlayer).generate()
            Thread.sleep(1_000)
            GeneratorTestGamePro(repositoryGame, generatorTestPlayer).generate()
            return
        }

        //03-28
        val date: List<String> = args.getOptionValues("source")[0]!!.split("-")

        //print("\n\n\nITALIA = ${date}\n\n\n")

        val month: String = date[0]
        print("\n\n\nmonth = ${month}\n")

        val day: String = date[1]
        print("day = ${day}\n\n\n")



//        val generatorPlayer = GeneratorPlayer(repositoryPlayer)
//        generatorPlayer.generate()
//        Thread.sleep(1_000)
//        val generatorGame = GeneratorGame(repositoryPlayer, repositoryGame)
//        generatorGame.generate()
    }
}

fun main(args: Array<String>) {
    runApplication<SpringerApplication>(*args)
}
