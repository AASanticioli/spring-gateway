package com.example.gateway


import co.touchlab.kermit.Logger
import com.example.gateway.person.PersonHandler
import com.example.gateway.person.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions.route
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.addRequestParameter
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import javax.sql.DataSource

@Configuration
class GatewayConfig {

    @Autowired
    lateinit var personRepository: PersonRepository

    @Bean
    fun helloRoute(): RouterFunction<ServerResponse> {
        return route()
            .GET("/hello/**", http("http://httpbin"))
            .before(
                rewritePath("/hello", "/get")
            )
            .before(
                addRequestParameter("hello", "world")
            )
            .before {
                Logger.i { "Incoming request: ${it.method().name()} ${it.uri().path}" }
                Logger.i { "Headers: ${it.headers()}" }
                it
            }
            .build()
    }

    @Bean
    fun personRoute(): RouterFunction<ServerResponse> {
        val handler = PersonHandler(personRepository)
        return router {
            accept(APPLICATION_JSON).nest {
                GET("/person/{id}", handler::retrieve)
                GET("/person", handler::list)
            }
            contentType(APPLICATION_JSON).nest {
                POST("/person", handler::create)
            }
        }
    }



}