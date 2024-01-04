package com.example.gateway

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration



@Configuration
class GatewayConfig(private val builder: RouteLocatorBuilder) {

    @Bean
    fun routes(): RouteLocator = builder.routes()
        .route( "my-route") { r -> r.path("/get/**").uri("http://httpbin/get")}
        .build()
}