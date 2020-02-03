package io.bahlsenwitz.springer.model.player

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.generator.util.GeneratorAvatar
import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.common.EntityUUID
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import kotlin.collections.HashMap

@Entity
@Table(name = "player")
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
class Player(
    id_player: UUID? = null,

    @Column(unique = true)
    var username: String,
    var password: String,

    @Type(type="text")
    @Column(columnDefinition = "text")
    var avatar: String = defaultAvatar(),

    var elo: Int = 1200,
    var rank: Int = 0,
    var disp: Int = 0,
    var date: String = PLACEHOLDER,

    @Column(insertable = true, updatable = true)
    var notify: Boolean = false,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config0: List<List<String>> = defaultConfig0(),
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config1: List<List<String>> = defaultConfig1(),
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config2: List<List<String>> = defaultConfig2(),

    //@Type(type = "jsonb")
    //@Column(columnDefinition = "jsonb")
    //var skinList: List<SKIN> = arrayListOf(SKIN.DEFAULT),
    //var email: String = PLACEHOLDER,
    //var name: String = PLACEHOLDER,
    //var surname: String = PLACEHOLDER,
    //var address: String = PLACEHOLDER,

    var device: String = PLACEHOLDER,
    var updated: String = PLACEHOLDER,
    var created: String = DATE_TIME_GENERATOR.rightNowString()

): EntityUUID(id_player), Comparable<Player> {

    companion object {
        const val PLACEHOLDER: String = "TBD"

       val DATE_TIME_GENERATOR = GeneratorDateTime()

        fun defaultConfig0(): List<List<String>> {
            val r1: List<String> = arrayListOf("","Bishop","Rook","Pawn","Pawn","Rook","Bishop","Pawn")
            val r0: List<String> = arrayListOf("Bishop","","Rook","Knight","King","Rook","","Bishop")
            return arrayListOf(r0, r1)
        }
        fun defaultConfig1(): List<List<String>> {
            val r1: List<String> = arrayListOf("Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn")
            val r0: List<String> = arrayListOf("Rook","Knight","Bishop","Queen","King","Bishop","Knight","Rook")
            return arrayListOf(r0, r1)
        }
        fun defaultConfig2(): List<List<String>> {
            val r1: List<String> = arrayListOf("","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop")
            val r0: List<String> = arrayListOf("","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop","King")
            return arrayListOf(r0, r1)
        }
        fun defaultAvatar(): String {
            val photoMap: HashMap<String,String> = GeneratorAvatar().photoMap
            val random = Random()
            return photoMap.entries.elementAt(random.nextInt(photoMap.size)).value
        }
    }

    override operator fun compareTo(other: Player): Int {
        if (this.elo == other.elo) {
            val date: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = this.created)
            val dateOther: ZonedDateTime = DATE_TIME_GENERATOR.generateDate(date = other.created)
            if(date.isBefore(dateOther)){
                return -1
            }
            return 1
        }
        if (this.elo > other.elo) {
            return -1
        }
        return 1
    }

    class Core(player: Player) {
        val id: String = player.id.toString()
        val username: String = player.username
        val avatar: String = player.avatar
        val elo: Int = player.elo
        val rank: Int = player.rank
        val date: String = player.date
        val disp: Int = player.disp
    }
}

