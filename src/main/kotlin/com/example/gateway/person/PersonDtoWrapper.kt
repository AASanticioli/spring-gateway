package com.example.gateway.person

import java.util.UUID

class PersonDtoWrapper {
    fun makePerson(dto: PersonDto): Person {
        return Person(
            id = dto.id?:UUID.randomUUID(), name = dto.name
        )
    }

    fun makePersonDto(person: Person): PersonDto {
        return PersonDto(
            id = person.id,
            name = person.name
        )
    }
}