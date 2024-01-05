package com.example.gateway

import co.touchlab.kermit.Logger
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.net.ConnectException


@Configuration
class GatewayConfig {

    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator {
        return builder
            .routes()
            .route("hello-route") {
                it.filters {
                    filter { exchange, chain ->
                        try {
                            val request = exchange.request
                            Logger.i { "Incoming request: ${request.method.name()} ${request.uri.path}" }
                            Logger.i { "Headers: ${request.headers}" }
                            Logger.i { "Body: ${request.body}" }
                            setPath("/get")
                            chain.filter(exchange)
                        } catch (e: ConnectException) {  // Handle connection errors
                            Logger.e("Failed to connect to downstream service:", e)
                            throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable", e)
                        } catch (e: IllegalArgumentException) { // Handle invalid arguments
                            Logger.e("Invalid request arguments:", e)
                            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request", e)
                        } catch (e: Exception) { // Handle other unexpected errors
                            Logger.e("Unexpected error during request processing:", e)
                            throw ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Error processing request",
                                e
                            )
                        }
                    }
                }
                it.path("/hello")
                it.uri("http://httpbin")
            }
            .route("additional-route") { // Separate route for other paths
                it.path("/**")
                it.uri("http://core/")
            }
            .build()
    }
}