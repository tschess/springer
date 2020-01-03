package io.bahlsenwitz.springer

//import io.bahlsenwitz.springer.model.Game
//import io.bahlsenwitz.springer.model.Player
//import java.util.*

import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryIapetus
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.util.*
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringerApplication(
    val repositoryPlayer: RepositoryPlayer,
    val repositoryIapetus: RepositoryIapetus,
    val repositoryGame: RepositoryGame
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val dataLoaderPlayer = DataLoaderPlayer(repositoryPlayer=repositoryPlayer)
        dataLoaderPlayer.importDataPlayer()
        val dataLoaderIapetus = DataLoaderIapetus(repositoryIapetus=repositoryIapetus,repositoryPlayer=repositoryPlayer)
        dataLoaderIapetus.importDataIapetus()
        val dataLoaderGame = DataLoaderGame(repositoryPlayer=repositoryPlayer,repositoryGame=repositoryGame)
        dataLoaderGame.importDataGame()

//        val dataGeneratorToy = DataGeneratorToy(
//            repositoryPlayer=repositoryPlayer,
//            repositoryIapetus=repositoryIapetus,
//            repositoryGame=repositoryGame)
//        dataGeneratorToy.defaultData()

        val timeoutTasks = TimeoutTasks(repositoryPlayer=repositoryPlayer,repositoryGame=repositoryGame)
        timeoutTasks.start()
    }
}

fun main(args: Array<String>) {
    runApplication<SpringerApplication>(*args)
}
