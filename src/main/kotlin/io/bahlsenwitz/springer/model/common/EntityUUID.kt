package io.bahlsenwitz.springer.model.common

import org.springframework.data.domain.Persistable
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class EntityUUID(givenId: UUID? = null): Persistable<UUID> {

    @Id
    @Column(name = "id", length = 16, unique = true, nullable = false)
    private val id: UUID = givenId ?: UUID.randomUUID()

    @Transient
    private var persisted: Boolean = givenId != null

    override fun getId(): UUID = id

    override fun isNew(): Boolean = !persisted

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other == null -> false
            other !is EntityUUID -> false
            else -> getId() == other.getId()
        }
    }

    override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"

    @PostPersist
    @PostLoad
    private fun setPersisted() {
        persisted = true
    }
}