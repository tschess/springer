package io.bahlsenwitz.springer.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class Iapetus(
    id: UUID? = null,

    @OneToOne
    @JoinColumn(name="owner")
    var owner: Player? = null,
    var nft: String? = null,
    var updated: String = "TBD"

): AssignedIdBaseEntity(id)