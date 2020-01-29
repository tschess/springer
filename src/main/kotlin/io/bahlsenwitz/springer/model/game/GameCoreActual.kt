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
            var date: String = game.date_update
            if (game.status == STATUS.PROPOSED) {
                invitation = true
                date = game.date_create
                if (game.challenger == CONTESTANT.WHITE) {
                    if (player == game.black) {
                        inbound = true
                    }
                    username = game.black.avatar
                    avatar = game.black.avatar
                }
                if (player == game.white) {
                    inbound = true
                    username = game.black.avatar
                    avatar = game.black.avatar
                }
                return Info(
                    invitation = invitation,
                    inbound = inbound,
                    date = date,
                    username = username,
                    avatar = avatar)
            }
            if (game.turn == CONTESTANT.WHITE) {
                if (player == game.white) {
                    inbound = true
                    username = game.black.avatar
                    avatar = game.black.avatar
                }
            }
            if (player == game.black) {
                inbound = true
            }
            return Info(
                invitation = invitation,
                inbound = inbound,
                date = date,
                username = username,
                avatar = avatar)
        }


    }
}