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

        val idWhite = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val usernameWhite = "white"
        val avatarWhite = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/mr_white.jpg")
        val playerWhite = Player(
            id = idWhite,
            username = usernameWhite,
            password = PASSWORD,
            avatar = avatarWhite,
            elo = 1200)
        repositoryPlayer.save(playerWhite)

        val idBlack = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val usernameBlack = "black"
        val avatarBlack = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/batman.png")
        val playerBlack = Player(
            id = idBlack,
            username = usernameBlack,
            password = PASSWORD,
            avatar = avatarBlack,
            elo = 1199)
        repositoryPlayer.save(playerBlack)

        val idTest =  UUID.fromString("22222222-2222-2222-2222-222222222222")
        val usernameTest = "test"
        val avatarTest = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_pink.png")
        val playerTest = Player(
            id = idTest,
            username = usernameTest,
            password = PASSWORD,
            avatar = avatarTest,
            elo = 1198)
        repositoryPlayer.save(playerTest)

        val usernameA = "aaa"
        val playerA = Player(
            username = usernameA,
            password = PASSWORD,
            elo = 1197)
        repositoryPlayer.save(playerA)

        val usernameB = "bbb"
        val playerB = Player(
            username = usernameB,
            password = PASSWORD,
            elo = 1196)
        repositoryPlayer.save(playerB)

        val usernameC = "ccc"
        val playerC = Player(
            username = usernameC,
            password = PASSWORD,
            elo = 1195)
        repositoryPlayer.save(playerC)

        val usernameD = "ddd"
        val playerD = Player(
            username = usernameD,
            password = PASSWORD,
            elo = 1194)
        repositoryPlayer.save(playerD)

        val usernameE = "eee"
        val playerE = Player(
            username = usernameE,
            password = PASSWORD,
            elo = 1193)
        repositoryPlayer.save(playerE)

        val usernameF = "fff"
        val playerF = Player(
            username = usernameF,
            password = PASSWORD,
            elo = 1192)
        repositoryPlayer.save(playerF)

        val username9 = "999"
        val player9 = Player(
            username = username9,
            password = PASSWORD,
            elo = 1191)
        repositoryPlayer.save(player9)

        val username8 = "888"
        val player8 = Player(
            username = username8,
            password = PASSWORD,
            elo = 1190)
        repositoryPlayer.save(player8)
    }

    private fun cleanCollections() {
        repositoryPlayer.deleteAll()
    }
}