package com.jess.first.kotlin.router

import com.jess.first.kotlin.handler.TestHandler
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

class DataRouter(private val handler: TestHandler) {

    fun route() = router {
        accept(TEXT_HTML).nest {
            GET("/datas", handler::datas)
            GET("/data", handler::data)
        }

        GET("/") {
            ok().body(Mono.just("welcome Home"), String::class.java)
        }

        "/api".nest {
            GET("/users", handler::findAll)
        }
    }
}