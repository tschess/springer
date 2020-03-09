package io.bahlsenwitz.springer.influx

class Influx {

    private val address: String = "http://localhost:8086/"

    //player.id
    fun activity(player_id: String, route: String) {
        try {
            khttp.post(
                url = "${this.address}write?db=tschess",
                data = "activity player=\"${player_id}\",route=\"${route}\""
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

    fun game(game_id: String, route: String) {
        try {
            //khttp.post(url = "${DateTime().INFLUX}write?db=tschess", data = "game id=\"${game.id}\",route=\"challenge\"")
            khttp.post(url = "${this.address}write?db=tschess", data = "game id=\"${game_id}\",route=\"${route}\"")
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

}