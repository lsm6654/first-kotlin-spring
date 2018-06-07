package com.jess.app.tutorial

// kotlin


// kotlin 은 핀란드 근처 러시아 섬 (java 커피? 인도네시아 섬?)
//2011 공식 발표 & 2012 오픈소스 (apache 2)
//2016 1.0 공식 릴리즈
//2017 1.1 릴리즈 - 안드로이드 공식 지원언어 발표
//2018 1.2 릴리즈

//2019+ 이후에 2.0을 발표 예정
//2.0.. 코틀린의 철학과 방향성을 파악할 수 있을듯한 중요한 지점

//부정적인 시선
// java++
// scala idiots(?) 스칼라만 못한..
// 짬뽕 언어..? (C# + scala + ....)

//긍정 시선
// better than java
// 간결함, 안전함, 다재다능함, 자바와의 100% 호환성 (코틀린은 자바와의 간극을 줄이기 위해 노력)
// fully open-source
// 쉬운 학습곡선
// https://www.slideshare.net/EdAustin2/kotlin-language-features-a-java-comparison


// JVM 기반이며 Java 와의 상호 운용이 100% 지원
// android, server side, javascript, native
// javascript 포팅 가능 (ecma script 5.1)
// native binary로 컴파일해서 IOS나 다른 임베디드 환경에서 사용할 수도 있음. (x86/ARM/Others)

//언어에서 지원해주는 기능이 많아서 DSL 도 많음

//기본 문법
//var(mutable), val(immutable) : 타입 추론.
//변수: 타입
fun sum(a: Int, b: Int): Int {

    val list = listOf(1,2,3)
    list.filter{ it > 1}
    list.filter{
        val shouldFilter = it > 0
        shouldFilter
    }

    return a + b
}




//