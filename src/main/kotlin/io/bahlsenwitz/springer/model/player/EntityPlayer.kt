package io.bahlsenwitz.springer.model.player

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.generator.common.GeneratorAvatar
import io.bahlsenwitz.springer.model.rating.EntityUUID
import io.bahlsenwitz.springer.util.ConfigState
import io.bahlsenwitz.springer.util.DateTime
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
    var date: String = this.date,

    @Column(insertable = true, updatable = true)
    var note: Boolean = true,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config0: List<List<String>> = chessConfig.defaultConfig0(),
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config1: List<List<String>> = chessConfig.defaultConfig1(),
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config2: List<List<String>> = chessConfig.defaultConfig2(),

    @Column(unique = true)
    var device: String? = null,

    var updated: String = this.date,
    var created: String = this.date

): EntityUUID(id), Comparable<Player> {

    companion object {

        val date: String = DateTime().getDate()
        val chessConfig: ConfigState = ConfigState()

        fun defaultAvatar(): String {
            val photoMap: HashMap<String,String> = GeneratorAvatar().photoMap
            val random = Random()
            return photoMap.entries.elementAt(random.nextInt(photoMap.size)).value
        }
    }

    override operator fun compareTo(other: Player): Int {
        if (this.elo == other.elo) {
            val dateSelf: ZonedDateTime = DateTime().getDate(this.created)
            val dateOther: ZonedDateTime = DateTime().getDate(other.created)
            if(dateSelf.isBefore(dateOther)){
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

