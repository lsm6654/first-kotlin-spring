package com.jess.first.kotlin.handler

import com.jess.first.kotlin.test.TestJava2
import com.jess.first.kotlin.test.TestJavaClass
import com.jess.first.kotlin.test.TestKotlinClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

//어노테이션 기반으로 아래와 같이 사용해도 된다.
//다른 방식의 빈 주입도 기존 자바 방식과 동일하게 가능
@RestController
class TestController(val testBean: TestKotlinClass) {

    @Autowired
    lateinit var testJavaClass: TestJavaClass

    @Autowired
    lateinit var testJava2: TestJava2

    @GetMapping("test")
    @ResponseBody
    fun test() : Mono<String> {

        testJavaClass.test()
        testJava2.test()
        testBean.test()

        return Mono.just("OK")
    }
}

@Configuration
class BeanCofigTest {

    @Bean
    fun test() = TestJava2()
}