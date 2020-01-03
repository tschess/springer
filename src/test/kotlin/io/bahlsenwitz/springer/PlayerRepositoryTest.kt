package io.bahlsenwitz.springer

//import io.bahlsenwitz.springer.repository.RepositoryGame
//import io.bahlsenwitz.springer.repository.RepositoryPlayer
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit4.SpringRunner
//
////import java.text.SimpleDateFormat
////import java.time.Duration
////import java.time.Instant
////import java.util.*
//
//
//@RunWith(SpringRunner::class)
//@SpringBootTest
//@AutoConfigureMockMvc
class PlayerRepositoryTest {

//    @Autowired
//    private lateinit var repositoryPlayer: RepositoryPlayer
//
//    @Autowired
//    private lateinit var repositoryGame: RepositoryGame
//
//    @Test
//    fun pushNotifications() {
//
//        val whiteId = UUID.fromString("11111111-1111-1111-1111-111111111111")
//        val whiteName = "white"
//        val whitePassword = "password"
//        val whiteAvatar = "https://github.com/tschess/catacombes/raw/master/mr_white.jpg"
//        val whiteDevice = "w"
//        val white = Player(id = whiteId, name = whiteName, password = whitePassword, avatar = whiteAvatar, device = whiteDevice)
//        repositoryPlayer.save(white)
//
//        val blackId = UUID.fromString("22222222-2222-2222-2222-222222222222")
//        val blackName = "black"
//        val blackPassword = "password"
//        val blackAvatar = "https://github.com/tschess/catacombes/raw/master/batman.png"
//        val blackDevice = "w"
//        val black = Player(id = blackId, name = blackName, password = blackPassword, avatar = blackAvatar, device = blackDevice)
//        repositoryPlayer.save(black)
//
//        val testId = UUID.fromString("00000000-0000-0000-0000-000000000000")
//        val test = Game(id = testId, black = black, white = white, clock = 24)
//        repositoryGame.save(test)
//
//        println("0: ${Duration.between(Instant.now(), test.created).seconds}")
//
//
//        val formatter = SimpleDateFormat("dd.MM.yyyy_HH:mm:ss.SSSS", Locale.getDefault())
//
//        val result = formatter.parse("04.11.2019_10:58:07.8470").toInstant()
//
//        println("result $result")
//
//        println("1: ${Duration.between(Instant.now(), result).seconds}")
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun myTest() {
//
//        val pageRequest0 = PageRequest.of(0, 10, Sort.by("elo").descending())
//        val output0 = playerRepository.findAll(pageRequest0)
//
//        println("~~~")
//        println(" 0 ")
//        for (element in output0.content) {
//            println("~~~")
//            println(element.elo)
//        }
//
//        val pageRequest1 = PageRequest.of(1, 10, Sort.by("elo").descending())
//        val output1 = playerRepository.findAll(pageRequest1)
//
//        println("~~~")
//        println(" 1 ")
//        for (element in output1.content) {
//            println("~~~")
//            println(element.elo)
//        }
//
//        val pageRequest2 = PageRequest.of(2, 10, Sort.by("elo").descending())
//        val output2 = playerRepository.findAll(pageRequest2)
//
//        println("~~~")
//        println(" 2 ")
//        for (element in output2.content) {
//            println("~~~")
//            println(element.elo)
//        }
//
//        val pageRequest3 = PageRequest.of(3, 10, Sort.by("elo").descending())
//        val output3 = playerRepository.findAll(pageRequest3)
//
//        println("~~~")
//        println(" 3 ")
//        for (element in output3.content) {
//            println("~~~")
//            println(element.elo)
//        }
//
//        val pageRequest4 = PageRequest.of(4, 10, Sort.by("elo").descending())
//        val output4 = playerRepository.findAll(pageRequest4)
//
//        println("~~~")
//        println(" 4 ")
//        for (element in output4.content) {
//            println("~~~")
//            println(element.elo)
//        }
//    }

}