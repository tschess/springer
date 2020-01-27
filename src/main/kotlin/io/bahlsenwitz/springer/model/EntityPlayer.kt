package io.bahlsenwitz.springer.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.util.PhotoGenerator
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
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
    var rank_position: Int = 0,
    var rank_displacement: Int = 0,
    var rank_date: String = "TBD",

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
    var skinList: List<String> = emptyList(),

    var device: String = "TBD",
    var updated: String = "TBD",
    var created: String = "TBD"

): AssignedIdBaseEntity(id) {
    companion object {
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
    }
}