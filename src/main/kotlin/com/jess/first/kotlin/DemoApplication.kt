package com.jess.first.kotlin

import com.jess.first.kotlin.config.beans
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.ipc.netty.http.server.HttpServer
import reactor.ipc.netty.tcp.BlockingNettyContext

// boot + annotation 방식은 어노테이션 붙이고 main 변경.
//@SpringBootApplication
class DemoKotlinApplication {

    private val httpHandler: HttpHandler
    private val server: HttpServer

    private var nettyContext: BlockingNettyContext? = null

    constructor(port: Int = 8080) {
        val context = GenericApplicationContext().apply {
            beans().initialize(this)
            refresh()
        }

        server = HttpServer.create(port)
        httpHandler = WebHttpHandlerBuilder
                .applicationContext(context)
                .apply { if (context.containsBean("corsFilter")) filter(context.getBean(CorsWebFilter::class.java)) }
                .build()

    }

    fun startAndAwait() {
        server.startAndAwait(ReactorHttpHandlerAdapter(httpHandler), { nettyContext = it })
    }
}


fun main(args: Array<String>) {

    DemoKotlinApplication().startAndAwait()

    // webMvc 방식은 위에 싹 지우고 어노테이션에 아래 함수만 실행시켜주면 됨.
    // runApplication<DemoKotlinApplication>(*args)
}