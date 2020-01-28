package io.bahlsenwitz.springer.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.util.PhotoGenerator
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

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
    var date: String = "TBD",

    var email: String = "TBD",
    var name: String = "TBD",
    var surname: String = "TBD",
    var address: String = "TBD",

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
    var skins: List<String> = emptyList(),

    var device: String = "TBD",
    var updated: String = "TBD",
    var created: String = rightNow()

): AssignedIdBaseEntity(id), Comparable<Player> {

    //TODO: wtf is companion object???
    companion object {

        val FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss.SSSS")
        val BROOKLYN = ZoneId.of("America/New_York")

        fun defaultConfig0(): List<List<String>> {
            val r0 = arrayListOf("","Bishop","Rook","Pawn","Pawn","Rook","Bishop","Pawn")
            val r1 = arrayListOf("Bishop","","Rook","Knight","King","Rook","","Bishop")
            return arrayListOf(r0, r1)
        }
        fun defaultConfig1(): List<List<String>> {
            val r0 = arrayListOf("Rook","Knight","Bishop","Queen","King","Bishop","Knight","Rook")
            val r1 = arrayListOf("Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn")
            return arrayListOf(r1, r0)
        }
        fun defaultConfig2(): List<List<String>> {
            val r0 = arrayListOf("","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop")
            val r1 = arrayListOf("","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop","King")
            return arrayListOf(r0, r1)
        }
        fun defaultAvatar(): String {
            val photoMap = PhotoGenerator().photoMap
            val random = Random()
            return photoMap.entries.elementAt(random.nextInt(photoMap.size)).value
        }

        fun rightNow(): String {
            return FORMATTER.format(ZonedDateTime.now(BROOKLYN))
        }
    }

    private fun generateDate(date: String): ZonedDateTime {
        if (date == "TBD") {
            return ZonedDateTime.now(BROOKLYN)
        }
        val dateTimeFormatted = LocalDateTime.parse(date, FORMATTER)
        return ZonedDateTime.of(dateTimeFormatted, BROOKLYN)
    }

    override operator fun compareTo(other: Player): Int {
        if (this.elo == other.elo) {
            val date: ZonedDateTime = generateDate(date = this.created)
            val dateOther: ZonedDateTime = generateDate(date = other.created)
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

