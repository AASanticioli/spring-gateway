package com.example.gateway.person

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import java.util.*
import kotlin.jvm.optionals.getOrNull

class PersonHandler(private val personRepository: PersonRepository) {
    private val mapper = jacksonObjectMapper()
    fun list(request: ServerRequest): ServerResponse {
        val entities = personRepository.findAll()
        val persons = entities.map { e -> PersonEntityWrapper().makePerson(e) }
        val dto = persons.map { p -> PersonDtoWrapper().makePersonDto(p) }
        val stringDto = mapper.writeValueAsString(dto)
        val json =
            if (persons.isNotEmpty()) {
                makeResponseJson(count = persons.size, propertyName = "data", propertyValue = stringDto)
            } else {
                makeResponseJson(count = 0, message = "No data found")
            }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(json)
    }

    fun create(request: ServerRequest): ServerResponse {
        val entityWrapper = PersonEntityWrapper()
        val dtoWrapper = PersonDtoWrapper()
        val personDto = request.body<PersonDto>()
        val person = PersonDtoWrapper().makePerson(personDto)
        val entity = entityWrapper.makePersonEntity(person)
        val persistedEntity = personRepository.save(entity)
        val persistedPerson = entityWrapper.makePerson(persistedEntity)
        val persistedDto = dtoWrapper.makePersonDto(persistedPerson)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(persistedDto)
    }

    fun retrieve(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id")
        val uuid = try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            null
        }
        val responseContent =
            if (uuid == null) {
                makeResponseJson(message = "The id '$id' is invalid")
            } else {
                val dtoWrapper = PersonDtoWrapper()
                val entityWrapper = PersonEntityWrapper()
                val entity = personRepository.findByUuid(uuid).getOrNull()
                if (entity == null) {
                    makeResponseJson(message = "No person found with the id '$id'")
                } else {
                    val person = entityWrapper.makePerson(entity)
                    val dto = dtoWrapper.makePersonDto(person)
                    val s = mapper.writeValueAsString(dto)
                    makeResponseJson(count = 1, propertyName = "data", propertyValue = s)
                }
            }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(responseContent)
    }

    private fun makeResponseJson(propertyName: String? = null, propertyValue: String? = null, message: String? = null, count: Int = 0): String {
        val objectNode = mapper.createObjectNode()
        objectNode.put("count", count)
        if (message != null) {
            objectNode.put("message", message)
        }
        if (propertyName != null) {
            objectNode.put(propertyName, propertyValue)
        }
        return mapper.writeValueAsString(objectNode)
    }
}
