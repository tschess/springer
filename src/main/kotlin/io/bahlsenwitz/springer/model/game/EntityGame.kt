package io.bahlsenwitz.springer.model.game

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.model.rating.EntityUUID
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

    @OneToOne
    @JoinColumn(name = "black")
    var black: Player,

    var challenger: CONTESTANT = CONTESTANT.BLACK,
    var winner: CONTESTANT? = null,
    var turn: CONTESTANT = CONTESTANT.WHITE,

    var on_check: Boolean = false,
    var highlight: String = "9999",

    var updated: String = this.date

) : EntityUUID(id) {

    fun isResolved(): Boolean {
        if(this.status == STATUS.RESOLVED){
            return true
        }
        return false
    }


    companion object : Comparator<Game>  {

        val dateTime: DateTime = DateTime()
        val date: String = this.dateTime.getDate()

        override fun compare(a: Game, b: Game): Int {

            val updateA: ZonedDateTime = dateTime.getDate(a.updated)
            val updateB: ZonedDateTime = dateTime.getDate(b.updated)
            val updateAB: Boolean = updateA.isBefore(updateB)

            val ongoingA: Boolean = a.status == STATUS.ONGOING
            val ongoingB: Boolean = b.status == STATUS.ONGOING

            val pendingA: Boolean = a.status == STATUS.PROPOSED
            val pendingB: Boolean = b.status == STATUS.PROPOSED

            //here...
            val preHistoryA: Boolean = a.status == STATUS.RESOLVED_WHITE || a.status == STATUS.RESOLVED_BLACK || a.status == STATUS.RESOLVED_WHITE_BLACK
            val preHistoryB: Boolean = b.status == STATUS.RESOLVED_WHITE || b.status == STATUS.RESOLVED_BLACK || b.status == STATUS.RESOLVED_WHITE_BLACK

            val historyA: Boolean = a.status == STATUS.RESOLVED
            val historyB: Boolean = b.status == STATUS.RESOLVED

            if(ongoingA && ongoingB){
                if(updateAB){
                    return -1
                }
                return 1
            }
            if(ongoingA && pendingB){
                return -1
            }
            if(ongoingA && preHistoryB){
                return -1
            }
            if(ongoingA && historyB){
                return -1
            }
            /* * */
            if(pendingA && ongoingB){
                return 1
            }
            if(pendingA && pendingB){
                if(updateAB){
                    return -1
                }
                return 1
            }
            if(pendingA && preHistoryB){
                return -1
            }
            if(pendingA && historyB){
                return -1
            }

            /* * */
            if(preHistoryA && ongoingB){
                return 1
            }
            if(preHistoryA && pendingB){
                return 1
            }
            if(preHistoryA && preHistoryB){
                if(updateAB){
                    return -1
                }
                return 1
            }
            if(preHistoryA && historyB){
                return -1
            }


            /* * */
            if(historyA && ongoingB){
                return 1
            }
            if(historyA && pendingB){
                return 1
            }
            if(historyA && preHistoryB){
                return -1
            }
            if(historyA && historyB){
                if(updateAB){
                    return 1
                }
                return -1
            }
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
    RESOLVED,
    RESOLVED_WHITE,
    RESOLVED_BLACK,
    RESOLVED_WHITE_BLACK
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

