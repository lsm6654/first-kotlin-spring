package com.jess.first.kotlin.handler

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

class TestHandler {

    private val users = Flux.just(
            User("Foo", "Foo", LocalDate.now().minusDays(1)),
            User("Bar", "Bar", LocalDate.now().minusDays(10)),
            User("Baz", "Baz", LocalDate.now().minusDays(100)))

    fun datas(req: ServerRequest): Mono<ServerResponse> {

        return ok().body(Mono.just("OK! GOOD"), String::class.java)
    }

    fun data(req: ServerRequest) = ServerResponse.ok().body(Mono.just("DATA!"), String::class.java)


    fun findAll(req: ServerRequest) = ok().body(users, User::class.java)
}


data class User(val firstName: String, val lastName: String, val birthDate: LocalDate)