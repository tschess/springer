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

    var updated: String = DateTime().getDate()

) : EntityUUID(id) {

    companion object : Comparator<Game>  {

        override fun compare(a: Game, b: Game): Int {
            val updateA: ZonedDateTime = DateTime().getDate(a.updated)
            val updateB: ZonedDateTime = DateTime().getDate(b.updated)
            val updateAB: Boolean = updateA.isBefore(updateB)

            if(a.status == b.status){
                if (updateAB) {
                    return -1
                }
                return 1
            }

            val ongoingA: Boolean = a.status == STATUS.ONGOING
            val ongoingB: Boolean = b.status == STATUS.ONGOING
            //val pendingB: Boolean = b.status == STATUS.PROPOSED
            val historyA: Boolean = a.status == STATUS.RESOLVED
            val historyB: Boolean = b.status == STATUS.RESOLVED

            if(ongoingA && !ongoingB){
                return -1
            }

            if(historyB && !historyA){
                return -1
            }
            return 0




            //if(ongoingA && !ongoingB){
                //return -1
            //}
            //if(ongoingA && ongoingB){
                //if (updateAB) {
                    //return -1
                //}
                //return 1
            //}

            //val pendingA: Boolean = a.status == STATUS.PROPOSED
            //val pendingB: Boolean = b.status == STATUS.PROPOSED

            //if(pendingA && !pendingB){
                //return -1
            //}

            //if(pendingA && pendingB){
                //if (updateAB) {
                    //return -1
                //}
                //return 1
            //}

            //val histoA: Boolean = a.status == STATUS.RESOLVED
            //val histoB: Boolean = b.status == STATUS.RESOLVED

            //if (histoA) { //histo a
                //if (histoB) { //histo b
                    //if (updateAB) {
                        //return 1 //b < a
                    //}
                    //return -1 //a < b
                //} //a is histo, b not
                //return 1 //b < a
            //} //neither a, nor b are histo...
            //return -1
            //return 0
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
    TBD,
    PUSH //first!
}

