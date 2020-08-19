package io.bahlsenwitz.springer.push

import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.player.Player

class Influx {

    fun activity(player: Player?, route: String) {
        if(player == null){
            return
        }
        try {
            khttp.post(
                url = "http://${ServerAddress().IP}:8086/write?db=tschess",
                data = "activity player=\"${player.id}\",route=\"${route}\""
            )
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

    fun growth(growth: String) {
        try {
            khttp.post(url = "http://${ServerAddress().IP}:8086/write?db=tschess", data = "growth client=\"${growth}\"")
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

    fun game(game: Game, route: String) {
        try {
            khttp.post(url = "http://${ServerAddress().IP}:8086/write?db=tschess", data = "game id=\"${game.id}\",route=\"${route}\"")
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

}