package io.bahlsenwitz.springer.model.game

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.model.common.EntityUUID
import io.bahlsenwitz.springer.model.player.Player
import io.bahlsenwitz.springer.util.DateTime
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

    @OneToOne
    @JoinColumn(name = "black")
    var black: Player,
    var black_elo: Int = getElo(black),
    var black_disp: Int? = null,//0,

    var challenger: CONTESTANT? = null,
    var winner: CONTESTANT? = null,
    var turn: CONTESTANT = CONTESTANT.WHITE,

    var on_check: Boolean = false,
    var highlight: String = "9999",

    var updated: String = DateTime().getDate()

) : EntityUUID(id) {

    companion object : Comparator<Game>  {

        fun getElo(player: Player): Int {
            return player.elo
        }

        override fun compare(a: Game, b: Game): Int {
            val updateA: ZonedDateTime = DateTime().getDate(a.updated)
            val updateB: ZonedDateTime = DateTime().getDate(b.updated)
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
            return -1
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

enum class CONDITION {
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

