package io.bahlsenwitz.springer.util

import io.bahlsenwitz.springer.model.Game
import io.bahlsenwitz.springer.model.Iapetus
import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryIapetus
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import java.util.*

class DataGeneratorToy(
    private val repositoryGame: RepositoryGame,
    private val repositoryIapetus: RepositoryIapetus,
    private val repositoryPlayer: RepositoryPlayer
) {

    private fun generateIapetusSkins() {
        val iapetusList = ArrayList<Iapetus>()

        //0
        val id_00: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")!!
        val iapetus_00 = Iapetus(id=id_00)
        iapetusList.add(iapetus_00)
        val id_01: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")!!
        val iapetus_01 = Iapetus(id=id_01)
        iapetusList.add(iapetus_01)
        val id_02: UUID = UUID.fromString("00000000-0000-0000-0000-000000000002")!!
        val iapetus_02 = Iapetus(id=id_02)
        iapetusList.add(iapetus_02)
        val id_03: UUID = UUID.fromString("00000000-0000-0000-0000-000000000003")!!
        val iapetus_03 = Iapetus(id=id_03)
        iapetusList.add(iapetus_03)
        val id_04: UUID = UUID.fromString("00000000-0000-0000-0000-000000000004")!!
        val iapetus_04 = Iapetus(id=id_04)
        iapetusList.add(iapetus_04)
        val id_05: UUID = UUID.fromString("00000000-0000-0000-0000-000000000005")!!
        val iapetus_05 = Iapetus(id=id_05)
        iapetusList.add(iapetus_05)
        val id_06: UUID = UUID.fromString("00000000-0000-0000-0000-000000000006")!!
        val iapetus_06 = Iapetus(id=id_06)
        iapetusList.add(iapetus_06)
        val id_07: UUID = UUID.fromString("00000000-0000-0000-0000-000000000007")!!
        val iapetus_07 = Iapetus(id=id_07)
        iapetusList.add(iapetus_07)
        val id_08: UUID = UUID.fromString("00000000-0000-0000-0000-000000000008")!!
        val iapetus_08 = Iapetus(id=id_08)
        iapetusList.add(iapetus_08)
        val id_09: UUID = UUID.fromString("00000000-0000-0000-0000-000000000009")!!
        val iapetus_09 = Iapetus(id=id_09)
        iapetusList.add(iapetus_09)

        //1
        val id_10: UUID = UUID.fromString("00000000-0000-0000-0000-00000000000A")!!
        val iapetus_10 = Iapetus(id=id_10)
        iapetusList.add(iapetus_10)
        val id_11: UUID = UUID.fromString("00000000-0000-0000-0000-00000000000B")!!
        val iapetus_11 = Iapetus(id=id_11)
        iapetusList.add(iapetus_11)
        val id_12: UUID = UUID.fromString("00000000-0000-0000-0000-00000000000C")!!
        val iapetus_12 = Iapetus(id=id_12)
        iapetusList.add(iapetus_12)
        val id_13: UUID = UUID.fromString("00000000-0000-0000-0000-00000000000D")!!
        val iapetus_13 = Iapetus(id=id_13)
        iapetusList.add(iapetus_13)
        val id_14: UUID = UUID.fromString("00000000-0000-0000-0000-00000000000E")!!
        val iapetus_14 = Iapetus(id=id_14)
        iapetusList.add(iapetus_14)
        val id_15: UUID = UUID.fromString("00000000-0000-0000-0000-00000000000F")!!
        val iapetus_15 = Iapetus(id=id_15)
        iapetusList.add(iapetus_15)
        val id_16: UUID = UUID.fromString("00000000-0000-0000-0000-000000000010")!!
        val iapetus_16 = Iapetus(id=id_16)
        iapetusList.add(iapetus_16)
        val id_17: UUID = UUID.fromString("00000000-0000-0000-0000-000000000020")!!
        val iapetus_17 = Iapetus(id=id_17)
        iapetusList.add(iapetus_17)
        val id_18: UUID = UUID.fromString("00000000-0000-0000-0000-000000000030")!!
        val iapetus_18 = Iapetus(id=id_18)
        iapetusList.add(iapetus_18)
        val id_19: UUID = UUID.fromString("00000000-0000-0000-0000-000000000040")!!
        val iapetus_19 = Iapetus(id=id_19)
        iapetusList.add(iapetus_19)

        //2
        val id_20: UUID = UUID.fromString("00000000-0000-0000-0000-000000000050")!!
        val iapetus_20 = Iapetus(id=id_20)
        iapetusList.add(iapetus_20)
        val id_21: UUID = UUID.fromString("00000000-0000-0000-0000-000000000060")!!
        val iapetus_21 = Iapetus(id=id_21)
        iapetusList.add(iapetus_21)
        val id_22: UUID = UUID.fromString("00000000-0000-0000-0000-000000000070")!!
        val iapetus_22 = Iapetus(id=id_22)
        iapetusList.add(iapetus_22)
        val id_23: UUID = UUID.fromString("00000000-0000-0000-0000-000000000080")!!
        val iapetus_23 = Iapetus(id=id_23)
        iapetusList.add(iapetus_23)
        val id_24: UUID = UUID.fromString("00000000-0000-0000-0000-000000000090")!!
        val iapetus_24 = Iapetus(id=id_24)
        iapetusList.add(iapetus_24)
        val id_25: UUID = UUID.fromString("00000000-0000-0000-0000-0000000000A0")!!
        val iapetus_25 = Iapetus(id=id_25)
        iapetusList.add(iapetus_25)
        val id_26: UUID = UUID.fromString("00000000-0000-0000-0000-0000000000B0")!!
        val iapetus_26 = Iapetus(id=id_26)
        iapetusList.add(iapetus_26)
        val id_27: UUID = UUID.fromString("00000000-0000-0000-0000-0000000000C0")!!
        val iapetus_27 = Iapetus(id=id_27)
        iapetusList.add(iapetus_27)
        val id_28: UUID = UUID.fromString("00000000-0000-0000-0000-0000000000D0")!!
        val iapetus_28 = Iapetus(id=id_28)
        iapetusList.add(iapetus_28)
        val id_29: UUID = UUID.fromString("00000000-0000-0000-0000-0000000000E0")!!
        val iapetus_29 = Iapetus(id=id_29)
        iapetusList.add(iapetus_29)

        //3
        val id_30: UUID = UUID.fromString("00000000-0000-0000-0000-0000000000F0")!!
        val iapetus_30 = Iapetus(id=id_30)
        iapetusList.add(iapetus_30)
        val id_31: UUID = UUID.fromString("00000000-0000-0000-0000-000000000100")!!
        val iapetus_31 = Iapetus(id=id_31)
        iapetusList.add(iapetus_31)
        val id_32: UUID = UUID.fromString("00000000-0000-0000-0000-000000000200")!!
        val iapetus_32 = Iapetus(id=id_32)
        iapetusList.add(iapetus_32)
        val id_33: UUID = UUID.fromString("00000000-0000-0000-0000-000000000300")!!
        val iapetus_33 = Iapetus(id=id_33)
        iapetusList.add(iapetus_33)
        val id_34: UUID = UUID.fromString("00000000-0000-0000-0000-000000000400")!!
        val iapetus_34 = Iapetus(id=id_34)
        iapetusList.add(iapetus_34)
        val id_35: UUID = UUID.fromString("00000000-0000-0000-0000-000000000500")!!
        val iapetus_35 = Iapetus(id=id_35)
        iapetusList.add(iapetus_35)
        val id_36: UUID = UUID.fromString("00000000-0000-0000-0000-000000000600")!!
        val iapetus_36 = Iapetus(id=id_36)
        iapetusList.add(iapetus_36)
        val id_37: UUID = UUID.fromString("00000000-0000-0000-0000-000000000700")!!
        val iapetus_37 = Iapetus(id=id_37)
        iapetusList.add(iapetus_37)
        val id_38: UUID = UUID.fromString("00000000-0000-0000-0000-000000000800")!!
        val iapetus_38 = Iapetus(id=id_38)
        iapetusList.add(iapetus_38)
        val id_39: UUID = UUID.fromString("00000000-0000-0000-0000-000000000900")!!
        val iapetus_39 = Iapetus(id=id_39)
        iapetusList.add(iapetus_39)

        //4
        val id_40: UUID = UUID.fromString("00000000-0000-0000-0000-000000000A00")!!
        val iapetus_40 = Iapetus(id=id_40)
        iapetusList.add(iapetus_40)
        val id_41: UUID = UUID.fromString("00000000-0000-0000-0000-000000000B00")!!
        val iapetus_41 = Iapetus(id=id_41)
        iapetusList.add(iapetus_41)
        val id_42: UUID = UUID.fromString("00000000-0000-0000-0000-000000000C00")!!
        val iapetus_42 = Iapetus(id=id_42)
        iapetusList.add(iapetus_42)
        val id_43: UUID = UUID.fromString("00000000-0000-0000-0000-000000000D00")!!
        val iapetus_43 = Iapetus(id=id_43)
        iapetusList.add(iapetus_43)
        val id_44: UUID = UUID.fromString("00000000-0000-0000-0000-000000000E00")!!
        val iapetus_44 = Iapetus(id=id_44)
        iapetusList.add(iapetus_44)
        val id_45: UUID = UUID.fromString("00000000-0000-0000-0000-000000000F00")!!
        val iapetus_45 = Iapetus(id=id_45)
        iapetusList.add(iapetus_45)
        val id_46: UUID = UUID.fromString("00000000-0000-0000-0000-000000001000")!!
        val iapetus_46 = Iapetus(id=id_46)
        iapetusList.add(iapetus_46)
        val id_47: UUID = UUID.fromString("00000000-0000-0000-0000-000000002000")!!
        val iapetus_47 = Iapetus(id=id_47)
        iapetusList.add(iapetus_47)
        val id_48: UUID = UUID.fromString("00000000-0000-0000-0000-000000003000")!!
        val iapetus_48 = Iapetus(id=id_48)
        iapetusList.add(iapetus_48)
        val id_49: UUID = UUID.fromString("00000000-0000-0000-0000-000000004000")!!
        val iapetus_49 = Iapetus(id=id_49)
        iapetusList.add(iapetus_49)

        // test
        val id_50: UUID = UUID.fromString("00000000-0000-0000-0000-000000005000")!!
        val ownerId = UUID.fromString("11111111-1111-1111-1111-111111111111")!!
        val owner: Player = repositoryPlayer.findById(ownerId).get()
        val iapetus_50 = Iapetus(id=id_50,owner=owner)
        iapetusList.add(iapetus_50)

        for (iapetus in iapetusList) {
            repositoryIapetus.save(iapetus)
        }
    }

    fun defaultData() {
        this.cleanCollections()

        val whiteId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val whiteName = "white"
        val whitePassword = "password"
        val whiteAvatar = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/mr_white.jpg")
        val whiteDevice = "w"
        val white =
            Player(id = whiteId, name = whiteName, password = whitePassword, avatar = whiteAvatar, device = whiteDevice)
        repositoryPlayer.save(white)

        val blackId = UUID.fromString("22222222-2222-2222-2222-222222222222")
        val blackName = "black"
        val blackPassword = "password"
        val blackAvatar = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/batman.png")
        val blackDevice = "w"
        val black =
            Player(id = blackId, name = blackName, password = blackPassword, avatar = blackAvatar, device = blackDevice)
        repositoryPlayer.save(black)

        val id_test = UUID.fromString("33333333-3333-3333-3333-333333333333")
        val name_test = "test"
        val password_test = "\$2a\$10\$paasde3Qy5jcxzZONo4a1OT3d4qgBIriGdyvO1qfeDWb2ksXSjycO"
        val avatar_test = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_pink.png")
        val device_test = "t"
        val player_test =
            Player(id = id_test, name = name_test, password = password_test, avatar = avatar_test, device = device_test)
        repositoryPlayer.save(player_test)

        val id_0 = UUID.fromString("0ee7d9ea-d0d9-4f68-a2f2-78c5f8f64231")
        val name_0 = "aesdfghjkl666"
        val password_0 = "\$2a\$10\$lVYVMFfU94mKfZHsybsP4eLlnf3QKtqc9l5BEwIa8/q7Vw/U.VmO2"
        val avatar_0 = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_zebra_double_blue_purple.png")
        val device_0 = "064697AB-61F1-4C20-8168-CD8401459FAF"
        val player_0 = Player(id = id_0, name = name_0, password = password_0, avatar = avatar_0, device = device_0)
        repositoryPlayer.save(player_0)

        val id_1 = UUID.fromString("ba650fed-c21d-48ed-86bb-a71b9bb82f52")
        val name_1 = "timxor"
        val password_1 = "\$2a\$10\$ps0SyMhkqL59iS1Tj4oQZeWjJTINL6jtLsgVdy6gLooYhJpdNOxne"
        val avatar_1 = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_zebra_double_purple_red.png")
        val device_1 = "F83E2182-7300-44F1-9D17-C902DACEA413"
        val player_1 = Player(id = id_1, name = name_1, password = password_1, avatar = avatar_1, device = device_1)
        repositoryPlayer.save(player_1)

        val id_2 = UUID.fromString("ce7e83ad-a030-446f-b6cf-b283b6cf95c8")
        val name_2 = "ernane"
        val password_2 = "\$2a\$10\$eUB3pIxUg/ITzUUr0KtmoePiK80JnXZeLP.acl/nJfG4lY1Bw1s/u"
        val avatar_2 = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_zebra_double_purple_red.png")
        val device_2 = "BC10A54D-A7E0-4954-B8EB-C546277EFB79"
        val player_2 = Player(id = id_2, name = name_2, password = password_2, avatar = avatar_2, device = device_2)
        repositoryPlayer.save(player_2)

        val id_3 = UUID.fromString("5071d574-90ce-429f-8c8c-c4576b2355ce")
        val name_3 = "alexandra"
        val password_3 = "\$2a\$10\$OVunCSr98/VV/71XY.dRxOIuSRt1j7bgxB/2FOdjnx0U2Ztf2RY7W"
        val avatar_3 = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_zebra_double_blue_red.png")
        val device_3 = "C5331E7A-125F-4167-8E6E-9458D1344E32"
        val player_3 = Player(id = id_3, name = name_3, password = password_3, avatar = avatar_3, device = device_3)
        repositoryPlayer.save(player_3)

        val id_4 = UUID.fromString("96ea4026-29a4-4939-a996-0f9777a5d758")
        val name_4 = "dwalter"
        val password_4 = "\$2a\$10\$uYaSm6TjsgSpmEiQVBv/6OiikfjI3gbIhohppeOu7ZeerbSDinM52"
        val avatar_4 = PhotoGenerator().getPhotoText("https://github.com/tschess/catacombes/raw/master/skull_purple.png")
        val device_4 = "BD89F13D-FDFB-4B25-874C-3E322F2D564A"
        val player_4 = Player(id = id_4, name = name_4, password = password_4, avatar = avatar_4, device = device_4)
        repositoryPlayer.save(player_4)

        generateIapetusSkins()

        val testId = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val test = Game(id = testId, black = black, white = white, clock = 24)
        repositoryGame.save(test)
    }

    private fun cleanCollections() {
        repositoryPlayer.deleteAll()
        repositoryIapetus.deleteAll()
        repositoryGame.deleteAll()
    }
}