package com.jess.app.tutorial

// 데이터를 담을 용도로 클래스를 만드는 경우가 있다
// 코틀린에서는 데이터용 클래스에 대한 유틸리티성 함수를 담은 데이터 클래스가 있다

// primary constructor 필요. (1개 이상의 파라미터 필요)

// equals(), hashCode()
// toString() format: Student(name=Jess, age=31)
// componentN() : destructuring용
// copy()  // immutable 한 객체에서 특정 필드값을 변경해서 새로운 객체 생


data class Student(val name: String, val age: Int)


//data class 프로퍼티도 정의할 수 있다
data class School(val name: String) {
    var age = 0
    fun showSchool() = 0
}


fun main(args: Array<String>) {

    val student1 = Student("jess", 20)

    //immutable 한 객체를 특정 필드만 변경하고 싶을 때, copy
    val student2 = student1.copy(name = "123")


    val school1 = School("배명")
    school1.age = 100

    val school2 = School("성남")
    school2.age = 10


    //componentN 함수를 통해 destructuring 이 가능하다.
    val (name, age) = student1
    println("name : $name, age : $age")
}



