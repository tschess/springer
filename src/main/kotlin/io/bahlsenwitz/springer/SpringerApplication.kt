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

    //InfluxDBFactory.connect(databaseURL, userName, password);
    //val influxDB: InfluxDBClientKotlin = InfluxDBClientKotlinFactory.create("http://3.12.121.89:8086", "sme", "111111".toCharArray())


    override fun run(args: ApplicationArguments?) {

        //val player_id: String = "123"
        //val fluxQuery: String = ("INSERT xxx player=${player_id}")
        //
        //influxDB.getQueryKotlinApi().query(fluxQuery, "my-org")
        khttp.post(url = "http://3.12.121.89:8086/write?db=tschess", data = "activity player=1989")
        //khttp.post(
            //url = "http://3.12.121.89:8086/write?db=tschess", "data-binary""activity player=888")

        val generatorTestPlayer = GeneratorTestPlayer(repositoryPlayer)
        generatorTestPlayer.generate()
        Thread.sleep(1_000)
        GeneratorTestGameFin(repositoryGame, generatorTestPlayer).generate()
        Thread.sleep(1_000)
        GeneratorTestGameAct(repositoryGame, generatorTestPlayer).generate()
        Thread.sleep(1_000)
        GeneratorTestGamePro(repositoryGame, generatorTestPlayer).generate()

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
