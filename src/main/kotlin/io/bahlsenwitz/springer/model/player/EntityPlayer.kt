package io.bahlsenwitz.springer.model.player

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.generator.util.GeneratorAvatar
import io.bahlsenwitz.springer.model.common.EntityUUID
import io.bahlsenwitz.springer.model.game.SKIN
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import org.springframework.data.jpa.repository.Temporal
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.TemporalType
import kotlin.collections.HashMap

@Entity
@Table(name = "player")
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
class Player(
    id: UUID? = null,

    @Column(unique = true)
    var username: String,
    var password: String,

    @Type(type="text")
    @Column(columnDefinition = "text")
    var avatar: String = defaultAvatar(),

    var elo: Int = 1200,
    var rank: Int = 0,
    var disp: Int = 0,
    @Temporal(TemporalType.TIMESTAMP)
    var date: Date = Date.from(ZonedDateTime.now(ZoneId.of("America/New_York")).toInstant()),

    @Column(insertable = true, updatable = true)
    var note: Boolean = false,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config0: List<List<String>> = defaultConfig0(),
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config1: List<List<String>> = defaultConfig1(),
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config2: List<List<String>> = defaultConfig2(),

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var skin: List<SKIN> = arrayListOf(SKIN.DEFAULT),

    @Column(unique = true)
    var device: String? = null,
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date = Date.from(ZonedDateTime.now(ZoneId.of("America/New_York")).toInstant()),
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date = Date.from(ZonedDateTime.now(ZoneId.of("America/New_York")).toInstant())

): EntityUUID(id), Comparable<Player> {

    companion object {

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
            val date: ZonedDateTime = this.created.toInstant().atZone(ZoneId.of("America/New_York"))
            val dateOther: ZonedDateTime = other.created.toInstant().atZone(ZoneId.of("America/New_York"))
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
}

