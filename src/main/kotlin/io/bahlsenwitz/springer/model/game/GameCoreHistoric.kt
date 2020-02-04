package io.bahlsenwitz.springer.model.game

import io.bahlsenwitz.springer.model.player.Player

class GameCoreHistoric(player: Player, game: Game) {
    private val info = getInfo(player, game)

    val game_id: String = game.id.toString()
    val date_end: String = game.updated

    val opponent_id: String = info.id
    val opponent_avatar: String = info.avatar
    val opponent_username: String = info.username
    val disp: Int = info.disp
    val odds: Int = info.odds

    val winner: Int = getWinner(player, game)

    companion object {

        data class Info(
            val id: String,
            val username: String,
            val avatar: String,
            val disp: Int,
            val odds: Int
        )

        fun getInfo(player: Player, game: Game): Info {
            if (game.white == player) {
                return Info(
                    id = game.black.id.toString(),
                    username = game.black.username,
                    avatar = game.black.avatar,
                    disp = game.white_disp!!,
                    odds = game.white_elo - game.black_elo
                )
            }
            return Info(
                id = game.white.id.toString(),
                username = game.white.username,
                avatar = game.white.avatar,
                disp = game.black_disp!!,
                odds = game.black_elo - game.white_elo
            )
        }

        fun getWinner(player: Player, game: Game): Int {
            if (game.winner == CONTESTANT.WHITE) {
                if (game.white == player) {
                    return 1
                }
                return -1
            }
            if (game.winner == CONTESTANT.BLACK) {
                if (game.black == player) {
                    return 1
                }
                return -1
            }
            return 0 //DRAW
        }
    }
}