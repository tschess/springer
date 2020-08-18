package io.bahlsenwitz.springer.push

import io.bahlsenwitz.springer.model.player.Player

class Influx {

    fun activity(player: Player?, route: String) {
        if(player == null){
            return
        }
        try {
            khttp.post(
                url = "${ServerAddress().IP}write?db=tschess",
                data = "activity player=\"${player.id}\",route=\"${route}\""
            )
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

    //}write?db=tschess", data = "growth player=
    fun growth(player: Player) {
        try {
            khttp.post(url = "${ServerAddress().IP}write?db=tschess", data = "growth player=\"${player.id}\"")
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

    //fun game(game: Game, route: String) {
        //try {
            //khttp.post(url = "${ServerAddress().IP}write?db=tschess", data = "game id=\"${game.id}\",route=\"${route}\"")
        //} catch (e: Exception) {
            //print(e.localizedMessage)
        //}
    //}

}