package io.bahlsenwitz.springer.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.model.common.EntityUUID
import io.bahlsenwitz.springer.model.common.SKIN
import io.bahlsenwitz.springer.util.DateTimeGenerator
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "game")
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
class Game(
    id: UUID? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var state: List<List<String>> = defaultState(),

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config: List<List<String>> = defaultConfig(),
    var status: STATUS = STATUS.PROPOSED,
    var outcome: OUTCOME = OUTCOME.TBD,
    var moves: Int = 0,

    @OneToOne
    @JoinColumn(name = "white")
    var white: Player,
    var white_disp: Int = 0,
    var white_elo: Int = 0,
    var white_skin: SKIN = SKIN.DEFAULT,

    @OneToOne
    @JoinColumn(name = "black")
    var black: Player,
    var black_disp: Int = 0,
    var black_elo: Int = 0,
    var black_skin: SKIN = SKIN.DEFAULT,

    var challenger: CONTESTANT = CONTESTANT.NA,
    var turn: CONTESTANT = CONTESTANT.WHITE,
    var winner: CONTESTANT = CONTESTANT.NA,

    var highlight: String = PLACEHOLDER,
    var check_on: Boolean = false,

    var date_start: String = PLACEHOLDER,
    var date_end: String = PLACEHOLDER,
    var date_update: String = PLACEHOLDER,
    var date_create: String = DATE_TIME_GENERATOR.rightNowString()

) : EntityUUID(id) {
    companion object {

        val DATE_TIME_GENERATOR = DateTimeGenerator()

        const val PLACEHOLDER: String = "TBD"

        fun defaultConfig(): List<List<String>> {
            val row0: List<String> =
                arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
            val row1: List<String> = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
            return arrayListOf(row1, row0)
        }

        fun defaultState(): List<List<String>> {
            val row0: List<String> =
                arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
            val row1: List<String> = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
            val row2: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row3: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row4: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row5: List<String> = arrayListOf("", "", "", "", "", "", "", "")
            val row6: List<String> = arrayListOf("Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn")
            val row7: List<String> =
                arrayListOf("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook")
            return arrayListOf(row0, row1, row2, row3, row4, row5, row6, row7)
        }
    }

    class Core(player: Player, game: Game) {
        private val info = getInfo(player, game)

        val game_id: String = game.id.toString()
        val date_end: String = game.date_end

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
                val odds: Int)

            fun getInfo(player: Player, game: Game): Info {
                if (game.white == player) {
                    return Info(
                        id = game.black.id.toString(),
                        username = game.black.username,
                        avatar = game.black.avatar,
                        disp = game.white_disp,
                        odds = game.white_elo - game.black_elo)
                }
                return Info(
                    id = game.white.id.toString(),
                    username = game.white.username,
                    avatar = game.white.avatar,
                    disp = game.black_disp,
                    odds = game.black_elo - game.white_elo)
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
                return 0
            }
        }
    }
}

enum class CONTESTANT {
    WHITE,
    BLACK,
    NA
}

enum class STATUS {
    PROPOSED,
    DECLINED, // expired or refused
    ONGOING,
    RESOLVED
}

enum class OUTCOME {
    CHECKMATE,
    TIMEOUT,
    RESIGN,
    DRAW,
    EXPIRED, //of an invitation
    REFUSED, //of an invitation
    TBD
}

