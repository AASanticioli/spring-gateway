package com.example.gateway.person

class PersonEntityWrapper {
    fun makePerson(entity: PersonEntity): Person {
        return Person(
            id = entity.uuid, name = entity.name
        )
    }

    fun makePersonEntity(person: Person): PersonEntity {
        return PersonEntity(
            uuid = person.id,
            name = person.name
        )
    }
}