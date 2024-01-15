package com.example.gateway.person

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PersonRepository : CrudRepository<PersonEntity, Long> {

    fun findByUuid(uuid: UUID): Optional<PersonEntity>
}