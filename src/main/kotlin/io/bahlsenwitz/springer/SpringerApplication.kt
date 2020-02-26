package io.bahlsenwitz.springer

import io.bahlsenwitz.springer.generator.backup.GeneratorGame
import io.bahlsenwitz.springer.generator.test.GeneratorTestGameAct
import io.bahlsenwitz.springer.generator.test.GeneratorTestGameFin
import io.bahlsenwitz.springer.generator.test.GeneratorTestGamePro
import io.bahlsenwitz.springer.generator.test.GeneratorTestPlayer
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.Zipper
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File


/**
 * default to loading the most recent back-up
 * if generate is specified - do that
 * can also supply an input file...
 */
@SpringBootApplication
class SpringerApplication(
    val repositoryPlayer: RepositoryPlayer,
    val repositoryGame: RepositoryGame
) : ApplicationRunner {

    //./gradlew bootRun --args='--source=28-03'
    override fun run(args: ApplicationArguments) {

        if (args.containsOption("source")) {

            val generatorTestPlayer = GeneratorTestPlayer(repositoryPlayer)
            generatorTestPlayer.generate()
            Thread.sleep(1_000)

            val date: List<String> = args.getOptionValues("source")[0]!!.split("-")

            val day: String = date[0]
            print("\n\n\nday = ${day}\n")
            val month: String = date[1]
            print("month = ${month}\n\n\n")

            File("..${File.separator}backup${File.separator + month + File.separator + day + File.separator}")
                .walk()
                .forEach {

                    if (it.extension == "zip") {
                        //println("${it.absolutePath}")
                        val file: File = Zipper().from(it)

                        GeneratorGame(repositoryPlayer, repositoryGame).generate(file)

                    }

                }
            return
        }


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

//        val generatorPlayer = GeneratorPlayer(repositoryPlayer)
//        generatorPlayer.generate()
//        Thread.sleep(1_000)
//        val generatorGame = GeneratorGame(repositoryPlayer, repositoryGame)
//        generatorGame.generate()