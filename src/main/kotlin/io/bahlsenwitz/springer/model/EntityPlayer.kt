package io.bahlsenwitz.springer.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Size

@Entity
@Table(name = "player")
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
class Player(

    id: UUID? = null,

    var rank: Int = 0,

    var address: String = "TBD",
    var api: String = "TBD",

    @Type(type="text")
    @Column(columnDefinition = "text")
    var avatar: String = "NONE",

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config0: List<List<String>> = default0(),

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config1: List<List<String>> = default1(),

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config2: List<List<String>> = default2(),

    @Size(max = 255)
    @Column(unique = true)
    var name: String,

    var password: String,
    var elo: Int = 1200,
    var tschx: Int = 4,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var squad: List<String> = arrayListOf("Amazon"),
    var device: String,
    var skin: String = "NONE",

    var updated: String = "TBD",
    var created: String = "TBD"

): AssignedIdBaseEntity(id) {
    companion object {
        fun default0(): List<List<String>> {
            val r0 = arrayListOf("","Bishop","Rook","Pawn","Pawn","Rook","Bishop","Pawn")
            val r1 = arrayListOf("Bishop","","Rook","Knight","King","Rook","","Bishop")
            return arrayListOf(r0, r1)
        }
        fun default1(): List<List<String>> {
            val r0 = arrayListOf("Rook","Knight","Bishop","Queen","King","Bishop","Knight","Rook")
            val r1 = arrayListOf("Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn")
            return arrayListOf(r1, r0)
        }
        fun default2(): List<List<String>> {
            val r0 = arrayListOf("","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop")
            val r1 = arrayListOf("","Bishop","Bishop","Bishop","Bishop","Bishop","Bishop","King")
            return arrayListOf(r0, r1)
        }
    }
}