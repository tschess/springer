package io.bahlsenwitz.springer.model.game

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.model.common.EntityUUID
import io.bahlsenwitz.springer.model.common.SKIN
import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
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
    var state: List<List<String>> = defaultState(), //don't actuvlly need this in historic... here can be null

    var status: STATUS = STATUS.PROPOSED,
    var outcome: OUTCOME = OUTCOME.TBD,
    var moves: Int = 0,

    @OneToOne
    @JoinColumn(name = "white")
    var white: Player,
    var white_elo: Int = getElo(white),
    var white_disp: Int = 0,
    var white_skin: SKIN = SKIN.DEFAULT,

    @OneToOne
    @JoinColumn(name = "black")
    var black: Player,
    var black_elo: Int = getElo(black),
    var black_disp: Int = 0,
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

        val DATE_TIME_GENERATOR = GeneratorDateTime()

        const val PLACEHOLDER: String = "TBD"

        fun getElo(player: Player): Int {
            return player.elo
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

