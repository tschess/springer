package io.bahlsenwitz.springer.model.game

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.model.common.EntityUUID
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.util.Constant
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "game")
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
class Game(
    id: UUID? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var state: List<List<String>>? = null,

    var status: STATUS = STATUS.PROPOSED,
    var condition: CONDITION = CONDITION.TBD,
    var moves: Int = 0,

    @OneToOne
    @JoinColumn(name = "white")
    var white: Player,
    var white_elo: Int = getElo(white),
    var white_disp: Int? = null,//0,
    var white_skin: SKIN = SKIN.DEFAULT,

    @OneToOne
    @JoinColumn(name = "black")
    var black: Player,
    var black_elo: Int = getElo(black),
    var black_disp: Int? = null,//0,
    var black_skin: SKIN = SKIN.DEFAULT,

    var challenger: CONTESTANT? = null,
    var winner: CONTESTANT? = null,
    var turn: CONTESTANT = CONTESTANT.WHITE,

    var on_check: Boolean = false,
    var highlight: String = PLACEHOLDER,

    var updated: String = Constant().getDate()

) : EntityUUID(id) {

    companion object : Comparator<Game>  {
        const val PLACEHOLDER: String = "TBD"

        fun getElo(player: Player): Int {
            return player.elo
        }

        override fun compare(a: Game, b: Game): Int {
            val updateA: ZonedDateTime = Constant().getDate(a.updated)
            val updateB: ZonedDateTime = Constant().getDate(b.updated)
            val updateAB: Boolean = updateA.isBefore(updateB)

            val histoA: Boolean = a.status == STATUS.RESOLVED
            val histoB: Boolean = b.status == STATUS.RESOLVED
            if (histoA) { //histo
                if (histoB) { //histo b
                    if (updateAB) {
                        return 1 //b < a
                    }
                    return -1 //a < b
                } //a is histo, b not
                return 1 //b < a
            } //neither a, nor b are histo...

            val propA: Boolean = a.status == STATUS.PROPOSED
            val propB: Boolean = b.status == STATUS.PROPOSED
            if (propA) { //histo
                if (propB) { //histo b
                    if (updateAB) {
                        return 1 //b < a
                    }
                    return -1 //a < b
                } //a is prop, b not
                return 1 //b < a
            } //neither a, nor b are prop...
            return 0
        }
    }
}

enum class CONTESTANT {
    WHITE,
    BLACK
}

enum class STATUS {
    PROPOSED,
    ONGOING,
    RESOLVED
}

enum class SKIN {
    DEFAULT,
    IAPETUS,
    CALYPSO,
    HYPERION,
    NEPTUNE
}

enum class CONDITION {
    CHECK, //currently in check...
    LANDMINE,
    CHECKMATE,
    TIMEOUT,
    RESIGN,
    DRAW,
    PENDING, //pending draw...
    EXPIRED, //of an invitation
    REFUSED, //of an invitation
    RESCIND, //of an invitation
    TBD
}

