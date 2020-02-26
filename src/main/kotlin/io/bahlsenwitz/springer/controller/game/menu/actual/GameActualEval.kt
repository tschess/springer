package io.bahlsenwitz.springer.controller.game.menu.actual

import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player
import java.util.*

class GameActualEval(val game: Game, val player: Player) : Game(
    id = game.id,
    state = game.state,
    status = game.status,
    outcome = game.outcome,
    moves = game.moves,
    white = game.white,
    white_elo = game.white_elo,
    white_disp = game.white_disp,
    white_skin = game.white_skin,
    black = game.black,
    black_elo = game.black_elo,
    black_disp = game.black_disp,
    black_skin = game.black_skin,
    challenger = game.challenger,
    winner = game.winner,
    turn = game.turn,
    on_check = game.on_check,
    highlight = game.highlight,
    updated = game.updated,
    created = game.created
) {

    data class Info(
        val invitation: Boolean,
        val inbound: Boolean,
        val date: String
    )

    val stats: Info = getInfo()

    private final fun getInfo(): Info {
        var inbound: Boolean = false
        var invitation: Boolean = false
        var date: String = game.updated

        if (game.status == STATUS.PROPOSED) { //invite
            invitation = true
            date = game.created
            if (game.challenger == CONTESTANT.WHITE) { //from white
                if (player == game.black) { //fetch by black
                    inbound = true
                    //invite, from white, fetch by black
                    return Info(
                        invitation = invitation,
                        inbound = inbound,
                        date = date
                    )
                } //fetch by white
                //invite, from white, fetch by white
                return Info(
                    invitation = invitation,
                    inbound = inbound,
                    date = date
                )
            } // from black
            if (player == game.white) {
                inbound = true

                //invite, from black, fetch by white
                return Info(
                    invitation = invitation,
                    inbound = inbound,
                    date = date
                )
            }
            //invite, from black, fetch by black
            return Info(
                invitation = invitation,
                inbound = inbound,
                date = date
            )
        } //game
        if (game.turn == CONTESTANT.WHITE) { //white move
            if (player == game.white) { //fetched by white
                inbound = true
                //game, white move, fetch by white
                return Info(
                    invitation = invitation,
                    inbound = inbound,
                    date = date
                )
            }
            //game, white move, fetch by black
            return Info(
                invitation = invitation,
                inbound = inbound,
                date = date
            )
        } //black move
        if (player == game.black) { //fetched by black
            inbound = true
            //game, black move, fetch by black
            return Info(
                invitation = invitation,
                inbound = inbound,
                date = date
            )
        }
        //game, black move, fetch by white
        return Info(
            invitation = invitation,
            inbound = inbound,
            date = date
        )
    }


}