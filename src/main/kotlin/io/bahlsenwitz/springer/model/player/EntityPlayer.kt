package io.bahlsenwitz.springer.model.player

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.generator.common.GeneratorAvatar
import io.bahlsenwitz.springer.model.rating.EntityUUID
import io.bahlsenwitz.springer.util.Config
import io.bahlsenwitz.springer.util.DateTime
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.ZonedDateTime
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

    @Type(type = "text")
    @Column(columnDefinition = "text")
    var avatar: String = defaultAvatar(),

    var elo: Int = 1200,
    var rank: Int = 0,
    var disp: Int = 0,
    var date: String = this.date,

    @Column(insertable = true, updatable = true)
    var note_value: Boolean = true,
    var note_key: String? = "POPUP", //"POPUP" means first game ~ ask them (overlay)

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config0: List<List<String>> = CONFIG.defaultConfig0(),
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config1: List<List<String>> = CONFIG.defaultConfig1(),
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config2: List<List<String>> = CONFIG.defaultConfig2(),

    @Column(unique = true)
    var device: String? = null,

    var updated: String = this.date,
    var created: String = this.date

) : EntityUUID(id), Comparable<Player> {

    companion object {

        val dateTime: DateTime = DateTime()
        val date: String = this.dateTime.getDate()
        val CONFIG: Config = Config()

        fun defaultAvatar(): String {
            val photoMap: HashMap<String, String> = GeneratorAvatar().photoMap
            val random = Random()
            return photoMap.entries.elementAt(random.nextInt(photoMap.size)).value
        }
    }

    override operator fun compareTo(other: Player): Int {
        if (this.elo == other.elo) {
            val dateSelf: ZonedDateTime = dateTime.getDate(this.created)
            val dateOther: ZonedDateTime = dateTime.getDate(other.created)
            if (dateSelf.isBefore(dateOther)) {
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

