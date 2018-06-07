package com.jess.app.tutorial

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.math.BigDecimal


//함수 선언
//default 선언 가능. 함수의 과부하 기능을 막아줌. 실제 함수 사용시 생략 가능하므로.
//return type  이 unit(void) 인경우 return 생략 가능 (실제 unit은 싱글톤 객체라 void랑 다르긴함)
fun plus(x: Int, y: Int = 10): Int {

    return x + y
}

//single expression (중괄호 없음)
fun double(x: Int): Int = x * 2


//infix (중위 표기법)
//제약사항이 있음. (파라미터는 1개만. 멤버 함수이거나 함수 extension 이어야함. default value 없어야함.)

class InfixTest(val y: Int) {

    infix fun test(x: Int): Int {
        return x + y
    }
}


//extension (밑)
infix fun Int.like(x: Int): Int {
    return this + x + x + x
}


//예제
fun Int.bd() :BigDecimal = BigDecimal(this)

val Int.bd: BigDecimal
    get() = BigDecimal(this)

//examples
fun main(args: Array<String>) {

    plus(x = 10, y = 20)

    val i = 100
    val you = 200
    val b = i like you      //i.like(you)
    val t = InfixTest(100)
    t test 100

    val c = i.max(50)


    //아래처럼 줄일 수도 있다.
    val money1 = Money(BigDecimal(100))
    val money2 = Money(100.bd())
    val money3 = Money(100.bd)

    aasd()

    //inline function
    operation { println("This is the actual op function") }



    println("fibonacci : ${fibonacci(8)}" )


    val json = """{"name":"example"}"""
    val a22 = readTo2<MyJsonType>(json)
    println(a22)
    readTo1(json)

    //아래는 classCastException 에러 발생
    //val b22 = readTo(json, MyJsonType::class.java)
    //println(b22)

    usefulExtensionFunctions(HashMap())
}








//extension function
fun Int.max(x: Int): Int = if (this > x) this else x


//extension function은 실제 타겟(function 입장에서는 receiver)에 function이 삽입되는 것이 아니다.
//local에 static function으로 바이트 코드가 생성되고, 해당 static function을 호출하는 것.
//java로 디컴파일한 코드 참고할 것.


//참고로 local function(package level) 은 파일명+Kt 접미어가 붙은 클래스 멤버 메소드가 된다. ExtensionTestKt.class 처럼..
//실제 클래스는 ExtensionTest 이고..




//function object
fun aasd() {
    println("a")
    val a = "a"
    val function = fun () {
        //function 안에 function 을 만들 수 있다.
        //outer function 의 변수에 접근도 가능.
        println("b $a")
    }

    fun bbsd() {
        println("b $a")
    }

    //equals
    function.invoke()
    bbsd()
}









//inline Function
//람다 파라미터를 가지고 있는 function 에서 작동하는 기능.
//실행한 메소드 안으로 코드를 삽입해주는 기능
//inline 키워드만 붙이면 활성화. 겉으로는 차이가 없고, 바이트코드에서 차이를 볼 수 있다.

inline fun operation(op: () -> Unit) {
    println("before calling op()")
    op()
    println("after calling op()")
}


//위 코드를 디컴파일하면 아래와 같다.
//main 으로 코드가 삽입되었다.(중간 op들어가는 부분 빼고)
// 그리고 invoke, checkParameterInNotNull 빠졌다
//inline 키워드를 뺀다면 main 함수에서는 operation 함수를 사용한 코드만 보인다.

//public final class InlineTestKt {
//    public static final void main(@NotNull String[] args) {
//        Intrinsics.checkParameterIsNotNull(args, "args");
//        String var1 = "before calling op()";
//        System.out.println(var1);
//        String var2 = "This is the actual op function";
//        System.out.println(var2);
//        var1 = "after calling op()";
//        System.out.println(var1);
//    }
//
//    public static final void operation(@NotNull Function0 op) {
//        Intrinsics.checkParameterIsNotNull(op, "op");
//        String var2 = "before calling op()";
//        System.out.println(var2);
//        op.invoke();
//        var2 = "after calling op()";
//        System.out.println(var2);
//    }
//}

//사용 이유는 속도
//고차 함수를 사용하면 런타임에 성능상 페널티 발생.
//각 함수는 객체이기 때문에 메모리 할당도 필요하고, 또 함수 body에서 접근하는 변수를 캡처하기 때문.
//위 성능상의 불이익을 해소하고자 한 것이 inline
//파라미터를 위한 함수 객체를 생성하고 호출을 생성하는 대신, 컴파일러가 코드를 호출 위치에 삽입해주는 것.

//inline 단점으로는, 컴파일된 코드양이 많아진다는 것. 컴파일 속도가 당연히 느려질 것
//그리고 자신을 recursive 하게 부를 수 없고, 다른 inline function에서도 호출이 불가능하다.



//reified
//자바는 컴파일시 제네릭 정보를 소거시킨다. (type erasure. Java 5 미만의 코드 하위 호환을 위해.)
//그래서 reflection을 통해서도 타입을 가져오지 못함.
//kotlin에서는 reified(구체화)를 통해서 타입을 가져올 수 있음.
//C#에 Reification 타입 구체화와 동일

//원래.
fun <T> readTo(str: String, t: T) : T? {

    //println(T::class)
    //jacksonObjectMapper().readValue(str)
    //위 둘다 compile error.

    return str as T
}

//그래서 타입이 아니라 클래스 정보 자체를 넘겨준다.
fun readTo1(str: String): MyJsonType {

    return jacksonObjectMapper().readValue(str, MyJsonType::class.java)
}

//reified를 써보자.
//inline 키워드가 반드시 있어야 하는데, reified 키워드와 함께 있다면
//바이트코드를 복사하면서 구체화하기 떄문에 컴파일러가 타입을 알 수 있다. (빌드 시점에 타입 정보를 남기는 것)
inline fun <reified T> readTo2(str: String) : T? {

    //코틀린은 아래와 같이 Type을 적어주지 않으면 컴파일 에러. (하위 호환성 고려안함)
    //var list:List
    println(T::class)
    jacksonObjectMapper().readValue(str, T::class.java)
    // 애초에 구체화 할수 있다면 T도 생략가능.
    // jacksonObjectMapper().readValue(str)

    return jacksonObjectMapper().readValue(str, T::class.java)
}


inline fun <reified T> printType(t: T) {

    when(t) {
        is String -> println("string : $t")
        is Int -> println("int : $t")
        is Student -> println("student : $t")
        else -> println("others")
    }

    println(t)
    println(T::class.java)
}


fun printTest() {
    printType("1")
    printType(1)
    printType(1L)
    printType(User("jess", 2))
}


data class MyJsonType(val name: String)






//tail recursion
//일반적인 재귀로 푼 피보나치
fun fibonacci(n: Int): Int {
    return if( n == 0 || n == 1) {
        n
    } else {
        fibonacci(n-1)
    }
}

// tailrec 키워드가 있으면 컴파일러는 꼬리 재귀를 최적화하여 루프 기반으로 변경.
// 스택 오버플로의 위험이 없으며 빠르고 효율적임.
// 그러나 재귀 호출 이후에 더 많은 코드가 있을때 재귀가 사용 안됨.. (컴파일러가 사용 안될때 경고 표시)
tailrec fun fibonacci2(n: Int): Int {
    return if (n == 0 || n == 1) {
        n
    } else {
        fibonacci2(n - 1)
    }
}












fun usefulExtensionFunctions(map: Map<String, Int>?) {

    //유용한 확장 함수들
    //scoping functions 이라고 부름.

    //let
    // 인자 : T (해당 객체)
    // 블록 : T.() -> R   //T 내부의 프로퍼티로 R의 결과값 도출
    // return : R

    //블록내에 객체 자신을 인자로 전달하고, 블록내 결과값을 반환.

    val abcd = HashMap<String, Int>()
    doSomething(abcd)

    //위의 내용을 아래처럼 쓸 수 있다.
    val a1 = 100
    val abb = HashMap<String, Int>().let {m ->
        val a1 =200

        //scope가 다르기 때문에 참조하는 local variable 이 다르다.
        println("a1 value in block : $a1")    //200
        doSomething(m)
    }
    println("a1 value : $a1")     //100

    //1개의 파라미터만 있다면 it 으로
    val abc = HashMap<String, Int>().let {
        doSomething(it)
    }
    //nullable 한경우에는 아래도 가능.
    map?.let{ doSomething(it)} ?: ""



    //apply
    // 인자 : this
    // 블록 : T.() -> Unit
    // return : this

    //특정 객체의 프로퍼티 접근하기 위해 사용.
    val aInstance = ExampleAA().apply {
        a = "abc"
        b = "abcd"
        c = "abb"
    }.apply {
        b = "abbd"
        //체이닝 가능. 프로퍼티 설정시 유용.
    }

    Student("jess", 20).apply {
        println(name)
        println(age)
    }

    //위의 코드는 아래와 같다.
    // boilerplate 구문 삭제를 도와준다.
    val property = ExampleAA()
    property.a = "abc"
    property.b = "abcd"
    property.c = "abb"

    val student = Student("jess2", 30)
    println(student.name)
    println(student.age)





    //run
    // 인자 : this
    // 블록 : T.() -> R
    // return : R

    // apply 처럼 새로운 객체 생성하여 반납하는 것은 비슷하지만, run은 이미 생성된 객체에 연속된 작업이 필요할 때 사용한다.
    // apply는 this를 반환하지만 run 은 새로운 결과값의 객체를 생성하여 반납.
    // let과 다른점은 this 차이
    val highSchool = School("배명")
    val others = School("모름")

    val school = School("카카오")
    school.run{
        if( name == "배명") highSchool else others
    }.showSchool()  //리턴타입에 따른 분기처리를 쉽게 할 수 있다. 마찬가지로 크진 않지만.. boilerplate 제거.

    //위의 코드는 아래와 같다.
    if(school.name == "배명")
        highSchool.showSchool()
    else
        others.showSchool()





    //also
    // 인자 : 객체
    // 블록 : (T) -> Unit
    // return : this
    StringBuilder().also {
        it.append("content: ")
        it.append(it.javaClass.canonicalName)
    }.print() // content: java.lang.StringBuilder


    StringBuilder().apply {
        append("content:")
        append(javaClass.canonicalName)
    }.print() // content: java.lang.StringBuilder


    //각 함수별 차이점
    //https://ask.ericlin.info/post/2017/06/subtle-differences-between-kotlins-with-apply-let-also-and-run
}

fun Any.print() = println(this)








data class Money(val amount: BigDecimal)