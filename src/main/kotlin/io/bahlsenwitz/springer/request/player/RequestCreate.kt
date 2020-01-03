package io.bahlsenwitz.springer.request.player

import com.squareup.moshi.JsonClass
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import kotlin.collections.ArrayList

@JsonClass(generateAdapter = true)
data class RequestCreate(
    @NotBlank(message = "username" )
    @get:Size(min = 3, max = 15, message = "username")
    val name: String,
    @NotBlank(message = "password" )
    @get:Size(min = 6, max = 20, message = "password")
    val password: String,
    val device: String,
    val avatar: String = getRandomAvatar(),
    val updated: String,
    val created: String,
    val api: String
) {
    companion object {
        private val avatarUrlList: ArrayList<String> = arrayListOf(
            "https://github.com/tschess/catacombes/raw/master/skull_pink.png",
            "https://github.com/tschess/catacombes/raw/master/skull.png",
            "https://github.com/tschess/catacombes/raw/master/skull_turquoise.png",
            "https://github.com/tschess/catacombes/raw/master/skull_admin.png",
            "https://github.com/tschess/catacombes/raw/master/skull_blue.png",
            "https://github.com/tschess/catacombes/raw/master/skull_green.png",
            "https://github.com/tschess/catacombes/raw/master/skull_invert.png",
            "https://github.com/tschess/catacombes/raw/master/skull_orange.png",
            "https://github.com/tschess/catacombes/raw/master/skull_purple.png",
            "https://github.com/tschess/catacombes/raw/master/skull_red.png",
            "https://github.com/tschess/catacombes/raw/master/skull_yellow.png",
            "https://github.com/tschess/catacombes/raw/master/skull_zebra_blue.png",
            "https://github.com/tschess/catacombes/raw/master/skull_zebra_double_blue_purple.png",
            "https://github.com/tschess/catacombes/raw/master/skull_zebra_double_blue_red.png",
            "https://github.com/tschess/catacombes/raw/master/skull_zebra_double_purple_green.png",
            "https://github.com/tschess/catacombes/raw/master/skull_zebra_double_purple_red.png",
            "https://github.com/tschess/catacombes/raw/master/skull_zebra_green.png",
            "https://github.com/tschess/catacombes/raw/master/skull_zebra_pink.png"
        )

        fun getRandomAvatar(): String {
            return avatarUrlList[Random().nextInt(
                avatarUrlList.size)]
        }
    }
}