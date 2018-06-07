package com.jess.app.tutorial

import java.io.File

//클래스 모양은 아래와 같다.
interface BaseUser {
    fun print()
}

//상속 및 구현은 : 뒤에. 다중 상속도 가능.
class User(val name: String, val age: Int): BaseUser {
    override fun print() {
    }

    init {
        //생성자를 호출하기 전 호출.
        println("Sample init")
    }
}

//근데 아무 클래스나 상속할 수 없다.

class AA {}
//class BB :AA {} // 에러남.
//기본적으로 모든 클래스는 final
//따라서 아래와 같이 open 키워드를 통해 재정의가 가능한 클래스라고 명시해줘야 한다. (함도 동일. 재정의가 가능한 함수는 open 을 사용해야 함.)
//(바이트코드에서보면 open 키워드 쓰지않으면 final)
open class AAA {}
class BB: AAA() {}


open class A2 {
    open fun f() { print("A") }
    fun a() { print("a") }
}

//sealed 키워드는 같은 파일안에서만 상속 가능.

interface B2 {
    fun f() { print("B") }
    fun b() { print("b") }
}

class C() : A2(), B2 {      //super 클래스는 반드시 한개여야 한다.
    // 같은 함수명은 컴파일러가 재정의가 필요하다고 알려준다. (함수명으로만 구분. 메소드 시그니쳐가 다르다고 오버로딩 되지 않음)
    // The compiler requires f() to be overridden:
    override fun f() {
        //다중상속일 경우 super 타입을 명시적으로 지정해주어야 한다.
        super<A2>.f() // call to A.f()
        super<B2>.f() // call to B.f()
    }
}


//프로퍼티에 대한 getter setter를 만들고 싶다면..
class D {
    var name: String = ""
        get() = if (name.isEmpty()) "name is null" else field
        set(value) {
            field = value
        }
}
class E(name: String) {
    //굳이 커스텀 setter/getter 가 필요하다면 이렇게도 가능.
    val name: String = name
        get() = field
}

class E2(val name: String)  //위와 동일




//생성자(constructor)
//constructor() 는 primary constructor 로 자동 생성.
class SampleClassA {

    //constructor()   //코드를 적지 않아도 자동 생성
}

//여러개의 생성자를 만들고 싶다면..
class SampleClass constructor() {

    //primary제외한 생성자는 모두 secondary constructor
    constructor(name: String): this() {
        println("name $name")
    }

    //val 키워드는 secondary constructor에서 사용 불가능.
    constructor(name: String, age: Int): this() {
        println("name $name, age $age")
    }
}

//constructor 키워드를 생략해서 아래처럼 해도 된다.
class SampleClassAB() {


    //primary제외한 생성자는 모두 secondary constructor
    constructor(name: String): this() {
        println("name $name")
    }

    //val 키워드는 secondary constructor에서 사용 불가능.
    constructor(name: String, age: Int): this() {
        println("name $name, age $age")
    }
}

//보통은..
class ServiceClass(val a: String, val b: String) {
}








class SampleClass2() {

    //아래 코드는 컴파일 에러. 프로퍼티는 반드시 초기화 필요.
    //val age: Int

    //늦추고 싶다면 lateinit 사용가능. 밑에서 설명.
    lateinit var name: String

    //secondary 생성자로 멤버 변수를 받고 싶다면 lateinit 할 수 있다.
    constructor(name: String, age: Int): this() {
        this.name = name
        println("name $name, age $age")
    }
}




//static
//1. object class (super type을 가질 수도 있음)
//2. companion object
//3. package method (scala, node 처럼)  -> 권장?

class Utils {
    //자바에서 사용시에는 Utils.Test.method1();
    object Test {
        fun method1() {
            println("object method called")
        }
    }

    companion object {      //object name을 줄수 있다.
        fun method1() {
            println("companion object method called")
        }

    }
}

//companion object는 패키지래밸에서 정의할수 없다. companion object Test {} //compile error 그래서 class 안에 정의.
//클래스와 함께 로드된다. (즉시 로딩)
//그러나 실제로 java에서 사용할 땐 static 처럼 사용할 수 없다. (Utils.Test.method1(); 로 사용 가능) @JvmStatic 을 메소드 이용해라.


//object는 인터페이스 구현도 가능. (java의 익명함수)
//혼자 구현가능 ( companion object와 비교해서 )
//expression (그래서 스스로 객체 구현 가능)
//lazy loading

//왜 object가 생겨났냐면,, 간단한 익명함수를 쉽게 개발 지원하기 위해서.
//자바에서 보통 작은 기능의 클래스가 필요하다면, 중첩클래스를 만드는데, 그 보완점..?
object AAA22: Runnable {
    override fun run() {
    }
}


//object
//thread safe  -> 싱글톤 객체 만들때 동기화하지 않아도 됨.
//  : 자바에서 static 생각하면 됨. 클래스로더에 올라갈때 인스턴스를 생성해서..
//lazy (아래 예시에서 ObjectClass가 사용되다고 Factory 오브젝트도 초기화되는게 아니라는 말)
//package level 에서도 가능.
class ObjectClass private constructor() {
    object Factory {
        init {
            println("object : lazy")
        }
        fun create() = ObjectClass()
    }

    object Factory2 {
    }
}


//companion object
// 클래스 안에서 하나의 객체를 정의하고 싶을 때. (하나의 클래스에 하나만 선언 가능)
//eager
class CompanionClass private constructor() {
    companion object : Factory<CompanionClass> {  //이름은 줘도 되고 안줘도 된다.
        init{
            println("companion object : eager")
        }
        override fun create(): CompanionClass = CompanionClass()
    }
}

interface Factory<T> {
    fun create(): T
}

//사용은 아래처럼
fun test4() = CompanionClass.create()







//typealias     타입 naming의 대안..? (타입명이 너무 길거나, 새로운 명칭을 사용하고 싶을떄)  //import 하면 어디서든 사용 가능.
typealias NodeSet = Set<Int>
typealias FileTable<K> = MutableMap<K, MutableList<File>>
typealias Predicate<T> = (T) -> Boolean

//single expression function (package level)
fun test(nodeSet: NodeSet) = 0
fun doSomeThing(abc: FileTable<Int>): Int = 0
fun double(x: Predicate<Int>) = 0








//by 위임 패턴을 위한 키워드
//상속받은 모든 public 메서드를 지정한 객체로 위임가능.
// Derived 객체 내부에 b 객체를 저장하고, b로 위임한 메서드를 Derived에 생성.
//b 는 대리객체가 됨.
interface Base {
    fun print()
}

class BaseImpl(val x: Int) : Base {
    override fun print() {
        println("baseImpl : $x ")
    }
}

class BaseImpl2(val x: Int) : Base {
    override fun print() {
        println("baseImpl2 : $x ")
    }
}

class Derived(b: Base) : Base by b {
    //fun print() = b.print() 이런 boilerplate code 삭제    //bytecode로 보면 Base를 상속받은 메소드에 위임받은 객체를 실행함.
    fun test() = println("234234")  // 새로 정의한 메서드도 가능
}






//lazy
class LazyInitialization {
    //kotlin은 non-null, nullable에 엄격하다.
    //private var name: String
    //위의 문법은 Property must be initialized or be abstract 컴파일 에러를 발생시킨다.

    //그러나 아래와같이 사용하고 필요한 순간 초기화시켜주면 된다.
    private lateinit var name: String
    //lateinit 키워드는 이 프로퍼티가 non-null이고 초기화를 나중에 할거라고 알려주는 것
    //초기화 코드를 넣어야 하는데 빼먹는다면 런타임에러 발생.. (실수 유발 가능)


    //그래서 lazy
    //lazy는 lateinit과 마찬가지로 초기화 지연할 때 사용하는 것인데, lateinit은 modifier, lazy는 함수임.
    private val lazyValue by lazy { println("lazy initialization") }

    //lazy는 초기화 해주는 코드가 같이 있다. (lateinit과 다르게)
    //실제 사용할때 프로퍼티를 초기화함 (객체 생성과 별개임)
    //lazy는 thread-safe함. (동기화하는 것이므로 당연히 오버헤드도 좀 있음)

    val lazyValue2: String by lazy {
        println("computed!")
        "Hello"
    }

    //computed!
    //Hello
    //Hello
    fun test() {
        println(lazyValue2)
        println(lazyValue2)
    }
    //lateinit은 var , lazy 는 val에 사용

}


//생성자를 받은 map을 가지고 타입별로 위임하여 특정 map으로 저장할 수 있다.
class UserAA(map: Map<String, Any?>) {
    val age: Int by map
    val name: String by map
}





fun main(args: Array<String>) {

    //map 은 Map<String, Any> 타입.
    val user = UserAA(mapOf(
            "name" to "jess",
            "age"  to 31
    ))
    println(user.name) // Prints "John Doe"
    println(user.age)  // Prints 25

}


@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD,
        AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.EXPRESSION)
@Retention(AnnotationRetention.SOURCE)
annotation class AutowiredTest(
        val id: String,
        val names: Array<String>
)


@AutowiredTest(id = "jess", names = ["lee", "jess", "etc"])
fun annotationTest() = 0


//enum 클래스는 그대로