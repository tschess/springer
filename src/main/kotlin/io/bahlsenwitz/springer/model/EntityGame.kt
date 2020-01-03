package io.bahlsenwitz.springer.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
class Game(

    id: UUID? = null,

    var clock: Int,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var state: List<List<String>> = defaultState(),

    var highlight: String = "NONE",

    @OneToOne
    @JoinColumn(name = "white")
    var white: Player,
    var white_update: String = "TBD",
    var white_message: String = "NONE",
    var white_posted: String = "TBD",
    var white_seen: Boolean = false,

    @OneToOne
    @JoinColumn(name = "black")
    var black: Player,
    var black_update: String = "TBD",
    var black_message: String = "NONE",
    var black_posted: String = "TBD",
    var black_seen: Boolean = false,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var config: List<List<String>> = black.config1,

    var check_on: String = "NONE",
    var turn: String = white.name,
    var status: String = "PROPOSED",
    var winner: String = "TBD",

    var catalyst: String = "NONE",
    var skin: String = "NONE",
    var updated: String = "TBD",
    var created: String = "TBD"

): AssignedIdBaseEntity(id) {
    companion object {
        fun defaultState(): List<List<String>> {
            val r0 = arrayListOf("","","","","","","","")
            val r1 = arrayListOf("","","","","","","","")
            val r2 = arrayListOf("","","","","","","","")
            val r3 = arrayListOf("","","","","","","","")
            val r4 = arrayListOf("","","","","","","","")
            val r5 = arrayListOf("","","","","","","","")
            val r6 = arrayListOf("","","","","","","","")
            val r7 = arrayListOf("","","","","","","","")
            return arrayListOf(r0,r1,r2,r3,r4,r5,r6,r7)
        }
    }
}