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

        val idWhite = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val usernameWhite = "white"
        val avatarWhite = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/mr_white.jpg")
        val playerWhite = Player(
            id = idWhite,
            username = usernameWhite,
            password = PASSWORD,
            avatar = avatarWhite,
            elo = 1200)
        repositoryPlayer.save(playerWhite)

        val idBlack = UUID.fromString("22222222-2222-2222-2222-222222222222")
        val usernameBlack = "black"
        val avatarBlack = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/batman.png")
        val playerBlack = Player(
            id = idBlack,
            username = usernameBlack,
            password = PASSWORD,
            avatar = avatarBlack,
            elo = 1199)
        repositoryPlayer.save(playerBlack)

        val idTest = UUID.fromString("33333333-3333-3333-3333-333333333333")
        val usernameTest = "test"
        val avatarTest = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_pink.png")
        val playerTest = Player(
            id = idTest,
            username = usernameTest,
            password = PASSWORD,
            avatar = avatarTest,
            elo = 1198)
        repositoryPlayer.save(playerTest)

        val idA = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        val usernameA = "aaa"
        val avatarA = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_zebra_double_blue_purple.png")
        val playerA = Player(
            id = idA,
            username = usernameA,
            password = PASSWORD,
            avatar = avatarA,
            elo = 1197)
        repositoryPlayer.save(playerA)

        val idB = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")
        val usernameB = "bbb"
        val avatarB = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_purple.png")
        val playerB = Player(
            id = idB,
            username = usernameB,
            password = PASSWORD,
            avatar = avatarB,
            elo = 1196)
        repositoryPlayer.save(playerB)

        val idC = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc")
        val usernameC = "ccc"
        val avatarC = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_yellow.png")
        val playerC = Player(
            id = idC,
            username = usernameC,
            password = PASSWORD,
            avatar = avatarC,
            elo = 1195)
        repositoryPlayer.save(playerC)

        val idD = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd")
        val usernameD = "ddd"
        val avatarD = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_red.png")
        val playerD = Player(
            id = idD,
            username = usernameD,
            password = PASSWORD,
            avatar = avatarD,
            elo = 1194)
        repositoryPlayer.save(playerD)

        val idE = UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee")
        val usernameE = "eee"
        val avatarE = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_turquoise.png")
        val playerE = Player(
            id = idE,
            username = usernameE,
            password = PASSWORD,
            avatar = avatarE,
            elo = 1193)
        repositoryPlayer.save(playerE)

        val idF = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")
        val usernameF = "fff"
        val avatarF = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_orange.png")
        val playerF = Player(
            id = idF,
            username = usernameF,
            password = PASSWORD,
            avatar = avatarF,
            elo = 1192)
        repositoryPlayer.save(playerF)

        val id9 = UUID.fromString("99999999-9999-9999-9999-999999999999")
        val username9 = "999"
        val avatar9 = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_blue.png")
        val player9 = Player(
            id = id9,
            username = username9,
            password = PASSWORD,
            avatar = avatar9,
            elo = 1191)
        repositoryPlayer.save(player9)

        val id8 = UUID.fromString("88888888-8888-8888-8888-888888888888")
        val username8 = "888"
        val avatar8 = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_green.png")
        val player8 = Player(
            id = id8,
            username = username8,
            password = PASSWORD,
            avatar = avatar8,
            elo = 1190)
        repositoryPlayer.save(player8)
    }

    private fun cleanCollections() {
        repositoryPlayer.deleteAll()
    }
}