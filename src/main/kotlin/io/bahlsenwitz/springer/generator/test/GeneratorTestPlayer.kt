package io.bahlsenwitz.springer.generator.test

import io.bahlsenwitz.springer.generator.util.GeneratorAvatar
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class GeneratorTestPlayer(private val repositoryPlayer: RepositoryPlayer) {

    private val PASSWORD = "\$2a\$10\$paasde3Qy5jcxzZONo4a1OT3d4qgBIriGdyvO1qfeDWb2ksXSjycO"
    val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val BROOKLYN = ZoneId.of("America/New_York")

    private var testPlayerList = mutableListOf<Player>()

    fun findByName(username: String): Player {
        return this.testPlayerList.filter { it.username == username }[0]
    }

    fun generate() {
        repositoryPlayer.deleteAll()

        val white = Player(
            id = UUID.fromString("00000000-0000-0000-0000-000000000000")!!,
            username = "white",
            password = PASSWORD,
            avatar = GeneratorAvatar().getPhotoText("https://github.com/tschess/catacombes/raw/master/mr_white.jpg"),
            elo = 1200,
            rank = 1,
            disp = 3,
            note = true,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(white) //0

        val black = Player(
            id = UUID.fromString("11111111-1111-1111-1111-111111111111")!!,
            username = "black",
            password = PASSWORD,
            avatar = GeneratorAvatar().getPhotoText("https://github.com/tschess/catacombes/raw/master/batman.png"),
            elo = 1199,
            rank = 2,
            disp = 1,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(black) //1

        val test = Player(
            id = UUID.fromString("22222222-2222-2222-2222-222222222222")!!,
            username = "test",
            password = PASSWORD,
            avatar = GeneratorAvatar().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_pink.png"),
            elo = 1198,
            rank = 3,
            disp = -3,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(test) //2

        val playerA = Player(
            username = "aaa",
            password = PASSWORD,
            elo = 1197,
            rank = 4,
            disp = -1,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(playerA) //3

        val playerB = Player(
            username = "bbb",
            password = PASSWORD,
            elo = 1196,
            rank = 5,
            disp = 0,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(playerB) //4

        val playerC = Player(
            username = "ccc",
            password = PASSWORD,
            elo = 1195,
            rank = 6,
            disp = -5,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(playerC) //5

        val playerD = Player(
            username = "ddd",
            password = PASSWORD,
            elo = 1194,
            rank = 7,
            disp = 1,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(playerD) //6

        val playerE = Player(
            username = "eee",
            password = PASSWORD,
            elo = 1193,
            rank = 8,
            disp = -3,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(playerE) //7

        val playerF = Player(
            username = "fff",
            password = PASSWORD,
            elo = 1192,
            rank = 9,
            disp = 1,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(playerF) //8

        val player9 = Player(
            id = UUID.fromString("99999999-9999-9999-9999-999999999999")!!,
            username = "999",
            password = PASSWORD,
            elo = 1191,
            rank = 10,
            disp = 0,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(player9) //9

        val player8 = Player(
            username = "888",
            password = PASSWORD,
            elo = 1190,
            rank = 11,
            disp = 3,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(player8) //10

        Thread.sleep(1_000)

        val player7 = Player(
            username = "777",
            password = PASSWORD,
            elo = 1190,
            rank = 12,
            disp = 3,
            date = FORMATTER.format(ZonedDateTime.now(BROOKLYN)).toString()
        )
        testPlayerList.add(player7) //11

        for (player: Player in this.testPlayerList) {
            repositoryPlayer.save(player)
        }
    }
}