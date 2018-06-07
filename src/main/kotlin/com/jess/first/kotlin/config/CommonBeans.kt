package com.jess.first.kotlin.config

import com.jess.first.kotlin.handler.TestHandler
import com.jess.first.kotlin.router.DataRouter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.reactive.function.server.RouterFunctions

fun beans() = org.springframework.context.support.beans {

    bean<DataRouter>()
    bean<TestHandler>()

    bean("webHandler") {
        RouterFunctions.toWebHandler(ref<DataRouter>().route())
    }

    profile("dev") {
        bean("corsFilter") {
            CorsWebFilter { CorsConfiguration().applyPermitDefaultValues() }
        }
    }
}