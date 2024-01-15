package com.example.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource


@SpringBootApplication
class GatewayApplication {
    @Bean
    fun dataSource(): DataSource {
        val builder = EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .setName("personDb")
            .addScript("schema.sql")
            .addScript("data.sql")
        return builder.build()
    }
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}