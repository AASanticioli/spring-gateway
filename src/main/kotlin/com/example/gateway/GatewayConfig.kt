package com.example.gateway


import co.touchlab.kermit.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions.route
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.addRequestParameter
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Configuration
class GatewayConfig {

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
}