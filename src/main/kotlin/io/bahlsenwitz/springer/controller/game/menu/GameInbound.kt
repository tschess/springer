package io.bahlsenwitz.springer.controller.game.menu

import io.bahlsenwitz.springer.model.game.CONTESTANT
import io.bahlsenwitz.springer.model.game.Game
import io.bahlsenwitz.springer.model.game.STATUS
import io.bahlsenwitz.springer.model.player.Player

class GameInbound(val game: Game, val player: Player) : Game(
    id = game.id,
    state = game.state,
    status = game.status,
    condition = game.condition,
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
    updated = game.updated
) {

    data class Info(
        val invitation: Boolean,
        val inbound: Boolean
    )

    val stats: Info = getInfo()

    private final fun getInfo(): Info {
        var inbound: Boolean = false
        var invitation: Boolean = false

        if (game.status == STATUS.PROPOSED) { //invite
            invitation = true
            if (game.challenger == CONTESTANT.WHITE) { //from white
                if (player == game.black) { //fetch by black
                    inbound = true
                    //invite, from white, fetch by black
                    return Info(
                        invitation = invitation,
                        inbound = inbound
                    )
                } //fetch by white
                //invite, from white, fetch by white
                return Info(
                    invitation = invitation,
                    inbound = inbound
                )
            } // from black
            if (player == game.white) {
                inbound = true

                //invite, from black, fetch by white
                return Info(
                    invitation = invitation,
                    inbound = inbound
                )
            }
            //invite, from black, fetch by black
            return Info(
                invitation = invitation,
                inbound = inbound
            )
        } //game
        if (game.turn == CONTESTANT.WHITE) { //white move
            if (player == game.white) { //fetched by white
                inbound = true
                //game, white move, fetch by white
                return Info(
                    invitation = invitation,
                    inbound = inbound
                )
            }
            //game, white move, fetch by black
            return Info(
                invitation = invitation,
                inbound = inbound
            )
        } //black move
        if (player == game.black) { //fetched by black
            inbound = true
            //game, black move, fetch by black
            return Info(
                invitation = invitation,
                inbound = inbound
            )
        }
        //game, black move, fetch by white
        return Info(
            invitation = invitation,
            inbound = inbound
        )
    }


}