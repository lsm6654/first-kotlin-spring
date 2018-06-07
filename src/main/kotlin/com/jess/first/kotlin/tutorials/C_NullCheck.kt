package com.jess.app.tutorial



// kotlin은 non-null, nullable에 엄격하다.
// kotlin은 NPE를 없애는게 목적.
// throw NullPointerException() 가 있지 않는한 발생하지 않는다.
fun test1(school: School?) {
    if(school != null) {
        println("str is ${school.age}") //if 문이 없다면, compiler error! null 이 들어올 가능성이 있기 때문에
    }
}




fun main(args: Array<String>) {

    val a = A1(B1(C1("str")))

    nullTest(a)
}

//? = @nullable
fun nullTest(a: A1?) {

    // 자바에서는 아래와 같이 null 체크 해야한다.
    // 코틀린은 if문도 expression
    val name =
            if (a != null && a.b != null && a.b.c != null) {
                a.b.c.name
            } else {
                null
            }
    println(name)

    // 위의 코드는 아래와 같다
    val name2 = a?.b?.c?.name
    println(name2)

    // null 말고 다른 값을 사용하고 싶을떄 ?:   elvis expression
    val name3 = a?.b?.c?.name ?: "else"
    println(name3)

    // null대신 NPE 발생하고 싶을때는
    val name4 = a!!.b!!.c!!.name
    println(name4)

    // 아니면 다른 Exception으로 처리하고 싶다면
    val name5 = a?.b?.c?.name ?: throw IllegalArgumentException("name is null")
    println(name5)
}







data class A1(val b: B1?)
data class B1(val c: C1?)
data class C1(val name: String?)