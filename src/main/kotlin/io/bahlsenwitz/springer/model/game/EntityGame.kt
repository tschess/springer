package io.bahlsenwitz.springer.model.game

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.bahlsenwitz.springer.generator.util.GeneratorDateTime
import io.bahlsenwitz.springer.model.common.EntityUUID
import io.bahlsenwitz.springer.model.common.SKIN
import io.bahlsenwitz.springer.model.player.Player
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "game")
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
class Game(
    id_game: UUID? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var state: List<List<String>>? = null,

    var status: STATUS = STATUS.PROPOSED,
    var outcome: OUTCOME = OUTCOME.TBD,
    var moves: Int = 0,

    @OneToOne
    @JoinColumn(name = "white")
    var white: Player,
    var white_elo: Int = getElo(white),
    var white_disp: Int? = null,//0,
    var white_skin: SKIN = SKIN.DEFAULT,

    @OneToOne
    @JoinColumn(name = "black")
    var black: Player,
    var black_elo: Int = getElo(black),
    var black_disp: Int? = null,//0,
    var black_skin: SKIN = SKIN.DEFAULT,

    var challenger: CONTESTANT? = null,
    var winner: CONTESTANT? = null,
    var turn: CONTESTANT = CONTESTANT.WHITE,

    var on_check: Boolean = false,
    var highlight: String = PLACEHOLDER,

    var date_start: String = PLACEHOLDER,
    var date_end: String = PLACEHOLDER,
    var date_update: String = PLACEHOLDER,
    var date_create: String = DATE_TIME_GENERATOR.rightNowString()

) : EntityUUID(id_game) {
    companion object {
        val DATE_TIME_GENERATOR = GeneratorDateTime()

        const val PLACEHOLDER: String = "TBD"

        fun getElo(player: Player): Int {
            return player.elo
        }
    }
}

enum class CONTESTANT {
    WHITE,
    BLACK
}

enum class STATUS {
    PROPOSED,
    DECLINED, //expired or refused
    ONGOING,
    PENDING, //pending draw...
    RESOLVED
}

enum class OUTCOME {
    CHECKMATE,
    TIMEOUT,
    RESIGN,
    DRAW,
    EXPIRED, //of an invitation
    REFUSED, //of an invitation
    TBD
}

