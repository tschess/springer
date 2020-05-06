package io.bahlsenwitz.springer.influx

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player

class Influx {

    private val address: String = "http://localhost:8086/"

    fun activity(player: Player, route: String) {
        try {
            khttp.post(
                url = "${this.address}write?db=tschess",
                data = "activity player=\"${player.id}\",route=\"${route}\""
            )
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

    fun growth(player_id: String) {
        try {
            khttp.post(url = "${this.address}write?db=tschess", data = "growth player=\"${player_id}\"")
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

    fun game(game: Game, route: String) {
        try {
            khttp.post(url = "${this.address}write?db=tschess", data = "game id=\"${game.id}\",route=\"${route}\"")
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

}