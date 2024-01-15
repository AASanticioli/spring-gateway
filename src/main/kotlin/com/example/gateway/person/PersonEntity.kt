package com.example.gateway.person

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*


@Table(name = "person")
@Entity
data class PersonEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val uuid: UUID,
    val name: String
) {
    constructor() : this(name = "", uuid = UUID.randomUUID())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        other as PersonEntity
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
