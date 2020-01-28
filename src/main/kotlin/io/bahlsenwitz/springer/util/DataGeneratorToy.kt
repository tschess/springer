package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.util.*

class DataGeneratorToy(
    private val repositoryPlayer: RepositoryPlayer
) {

    fun defaultData() {
        this.cleanCollections()

        val PASSWORD = "\$2a\$10\$paasde3Qy5jcxzZONo4a1OT3d4qgBIriGdyvO1qfeDWb2ksXSjycO"

        //0
        val idWhite = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val usernameWhite = "white"
        val avatarWhite = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/mr_white.jpg")
        val playerWhite = Player(
            id = idWhite,
            username = usernameWhite,
            password = PASSWORD,
            avatar = avatarWhite,
            elo = 1200,
            rank = 1,
            disp = 3)
        repositoryPlayer.save(playerWhite)

        //1
        val idBlack = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val usernameBlack = "black"
        val avatarBlack = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/batman.png")
        val playerBlack = Player(
            id = idBlack,
            username = usernameBlack,
            password = PASSWORD,
            avatar = avatarBlack,
            elo = 1199,
            rank = 2,
            disp = 1)
        repositoryPlayer.save(playerBlack)

        //2
        val idTest =  UUID.fromString("22222222-2222-2222-2222-222222222222")
        val usernameTest = "test"
        val avatarTest = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_pink.png")
        val playerTest = Player(
            id = idTest,
            username = usernameTest,
            password = PASSWORD,
            avatar = avatarTest,
            elo = 1198,
            rank = 3,
            disp = -3)
        repositoryPlayer.save(playerTest)

        //3
        val usernameA = "aaa"
        val playerA = Player(
            username = usernameA,
            password = PASSWORD,
            elo = 1197,
            rank = 4,
            disp = -1)
        repositoryPlayer.save(playerA)

        //4
        val usernameB = "bbb"
        val playerB = Player(
            username = usernameB,
            password = PASSWORD,
            elo = 1196,
            rank = 5,
            disp = 0)
        repositoryPlayer.save(playerB)

        //5
        val usernameC = "ccc"
        val playerC = Player(
            username = usernameC,
            password = PASSWORD,
            elo = 1195,
            rank = 6,
            disp = -5)
        repositoryPlayer.save(playerC)

        //6
        val usernameD = "ddd"
        val playerD = Player(
            username = usernameD,
            password = PASSWORD,
            elo = 1194,
            rank = 7,
            disp = 1)
        repositoryPlayer.save(playerD)

        //7
        val usernameE = "eee"
        val playerE = Player(
            username = usernameE,
            password = PASSWORD,
            elo = 1193,
            rank = 8,
            disp = -3)
        repositoryPlayer.save(playerE)

        //8
        val usernameF = "fff"
        val playerF = Player(
            username = usernameF,
            password = PASSWORD,
            elo = 1192,
            rank = 9,
            disp = 1)
        repositoryPlayer.save(playerF)

        //9
        val username9 = "999"
        val player9 = Player(
            username = username9,
            password = PASSWORD,
            elo = 1191,
            rank = 10,
            disp = 0)
        repositoryPlayer.save(player9)

        //10
        val username8 = "888"
        val player8 = Player(
            username = username8,
            password = PASSWORD,
            elo = 1190,
            rank = 11,
            disp = 3)
        repositoryPlayer.save(player8)

        Thread.sleep(1_000)

        //11
        val username7 = "777"
        val player7 = Player(
            username = username7,
            password = PASSWORD,
            elo = 1190,
            rank = 11,
            disp = 3)
        repositoryPlayer.save(player7)
    }

    private fun cleanCollections() {
        repositoryPlayer.deleteAll()
    }
}