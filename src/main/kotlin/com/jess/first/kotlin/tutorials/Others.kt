package com.jess.app.tutorial


fun destructuringTest() {

    //destructuring
    val person = Person("1", 2, "3")
    val (name, age, address) = person  //변수는 독립적으로 사용할 수 있다. 명칭이 뭔지?? multiple variables 라고만 나옴..

    //data class가 빌드되면 실제로 아래의 메소드들을 생성한다.
    person.component1()
    person.component2()
    person.component3()
    //50개까지 만들어봤는데 max가 어딜지 모르겠음.. 안찾아짐ㅠ

    println(name + age + address)

    val map = mapOf("1" to 1, "2" to 2)
    for((k,v) in map){
        //TODO something
    }

    //underscore for unused variables
    val (name1, age1, _) = person   //안쓰는데 변수 선언하면 unused 뜨니까...
}


//ClosedRange
fun rangeTest() {
    val value = 0
    if (value in 1..10) { } // equals 1 <= i && i <= 10

    for (i in 1..4) print(i)

    for (i in 4 downTo 1) print(i) //4,3,2,1

    for (i in 1..4 step 2) print(i)
}




fun typeCheckAndCastTest(any: Any) {

    //smart cast
    if (any is String) {
        println(any.length) //자동으로 String으로 캐스팅되어있음. -> when 구문에서 잘쓰일수 있음.
    }

    //unsafe cast
    val x = any as Int   //캐스팅불가능한 경우 ClassCastException 발생

    //safe cast
    val y = any as? String  //캐스팅불가능한 경우 null
    println(y)
}




//java switch
fun whenTest(x: Any): Int {

    when (x) {
        0,1 -> println("one or two")
        is String -> println("is String")
        else -> print("s does not encode x")

    }

    val y = when(x) {
        0,1 -> 1
        is String -> 2
        else -> 0
    }


    when (x) {
        0,1 -> return 0
        is String -> return 1
        else -> throw IllegalArgumentException("unknown")

    }

    return when (x) {
        0,1 -> 0
        is String -> 1
        else -> 2

    }
}









fun doSomething(map: Map<String, Int>) = 0

fun main(args: Array<String>) {
//    destructuringTest()
//    rangeTest()
//    typeCheckAndCastTest(1)

    whenTest(1)
}

data class Person(val name: String, val age: Int, val address: String)


class ExampleAA(){
    lateinit var a: String
    lateinit var b: String
    lateinit var c: String
}
