package io.bahlsenwitz.springer

import io.bahlsenwitz.springer.controller.game.backup.GameBackUp
import io.bahlsenwitz.springer.controller.player.backup.PlayerBackUp
import io.bahlsenwitz.springer.generator.backup.GeneratorGame
import io.bahlsenwitz.springer.generator.backup.GeneratorPlayer
import io.bahlsenwitz.springer.generator.backup.Zipper
import io.bahlsenwitz.springer.generator.test.GeneratorTestGameAct
import io.bahlsenwitz.springer.generator.test.GeneratorTestGameFin
import io.bahlsenwitz.springer.generator.test.GeneratorTestGamePro
import io.bahlsenwitz.springer.generator.test.GeneratorTestPlayer
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.schedule.Schedule
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File
import javax.annotation.PreDestroy

/**
 * TODO: - the following is a *roadmap* task -
 * default to loading the most recent back-up
 * if generate is specified - do that
 * can also supply an input file...
 */
@SpringBootApplication
class SpringerApplication(
    val repositoryPlayer: RepositoryPlayer,
    val repositoryGame: RepositoryGame
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        if (args.containsOption("source")) { //./gradlew bootRun --args='--source=28-03'
            val date: List<String> = args.getOptionValues("source")[0]!!.split("-")
            val day: String = date[0]
            val month: String = date[1]
            File("..${File.separator}backup${File.separator + month + File.separator + day + File.separator}")
                .walkBottomUp()
                .forEach {
                    if (it.extension == "zip") {
                        //println("---> ${it.absolutePath}")
                        val file: File = Zipper().from(it)
                        if (it.name.contains("player")) {
                            GeneratorPlayer(repositoryPlayer).generate(file)
                        }
                        if (it.name.contains("game")) {
                            GeneratorGame(repositoryPlayer, repositoryGame).generate(file)
                        }
                    }
                }
            Schedule(repositoryPlayer, repositoryGame).execute()
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
        Thread.sleep(1_000)
        Schedule(repositoryPlayer, repositoryGame).execute()
    }

    @PreDestroy
    fun onExit() {
        print("\n\n\n###STOPing###")
        try {
            PlayerBackUp(repositoryPlayer).backup()
            GameBackUp(repositoryGame).backup()
        } catch (e: InterruptedException) {
            print(e.stackTrace)
        }
        print("###STOP FROM THE LIFECYCLE###\n\n\n")
    }
}

fun main(args: Array<String>) {
    runApplication<SpringerApplication>(*args)
}