package io.bahlsenwitz.springer.model.game

import io.bahlsenwitz.springer.model.player.Player

class GameCoreActual(player: Player, game: Game) {
    private val info = getInfo(player, game)

    val id: String = game.id.toString()
    val invitation: Boolean = info.invitation
    val inbound: Boolean = info.inbound
    val date: String = info.date
    val username: String = info.username
    val avatar: String = info.avatar

    companion object {

        data class Info(
            val invitation: Boolean,
            val inbound: Boolean,
            val date: String,
            val username: String,
            val avatar: String
        )

        fun getInfo(player: Player, game: Game): Info {
            var inbound: Boolean = false
            var invitation: Boolean = false
            var username: String = game.white.username
            var avatar: String = game.white.avatar
            var date: String = game.updated

            if (game.status == STATUS.PROPOSED) { //invite
                invitation = true
                date = game.created
                if (game.challenger == CONTESTANT.WHITE) { //from white
                    if (player == game.black) { //fetch by black
                        inbound = true
                        //invite, from white, fetch by black
                        return Info(invitation = invitation, inbound = inbound, date = date, username = username, avatar = avatar)
                    } //fetch by white
                    username = game.black.username
                    avatar = game.black.avatar
                    //invite, from white, fetch by white
                    return Info(invitation = invitation, inbound = inbound, date = date, username = username, avatar = avatar)
                } // from black
                if (player == game.white) {
                    inbound = true
                    username = game.black.username
                    avatar = game.black.avatar
                    //invite, from black, fetch by white
                    return Info(invitation = invitation, inbound = inbound, date = date, username = username, avatar = avatar)
                }
                //invite, from black, fetch by black
                return Info(invitation = invitation, inbound = inbound, date = date, username = username, avatar = avatar)
            } //game
            if (game.turn == CONTESTANT.WHITE) { //white move
                if (player == game.white) { //fetched by white
                    inbound = true
                    username = game.black.username
                    avatar = game.black.avatar
                    //game, white move, fetch by white
                    return Info(invitation = invitation, inbound = inbound, date = date, username = username, avatar = avatar)
                }
                //game, white move, fetch by black
                return Info(invitation = invitation, inbound = inbound, date = date, username = username, avatar = avatar)
            } //black move
            if (player == game.black) { //fetched by black
                inbound = true
                //game, black move, fetch by black
                return Info(invitation = invitation, inbound = inbound, date = date, username = username, avatar = avatar)
            }
            //game, black move, fetch by white
            username = game.black.username
            avatar = game.black.avatar
            return Info(invitation = invitation, inbound = inbound, date = date, username = username, avatar = avatar)
        }



    }
}