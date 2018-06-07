package com.jess.app.tutorial

import kotlinx.coroutines.experimental.*
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import java.util.concurrent.CompletableFuture

//자바와 다른 동시성 프로그래밍
//coroutine 은 경량화 스레드라고 생각할 수 있다.
//코루틴 개념과 용어는 이미 1958년에 나옴. //중단가능한 function이다. (잠시 중지되어 나중에 재시작될 수 있고, 개입 중단 가능한 루틴.)
//스레드와 마찬가지로 병렬로 실행되고, 중단도 시킬 수 있다.
//스레드는 native 스레드(os단)에 직접 매핑되어 관리하는 반면, 코루틴은 native 스레드에 직접 매핑되지 않아서 스레드가 도입한 오버헤드가 없다.
//코루틴은 스레드 컨텍스트 전환이 없다. 보통 1스레드가 1메가 바이트 이상 소모하는걸 고려하면 굉장히 부담이 적음.

//일반적인 서브루틴은 호출될 때마다 메소드의 처음부터 실행을 시작하고 리턴될 때 항상 호출자(caller)로 돌아감.
//코루틴은 이전 상태를 기억하고 있어서 호출시 이전 호출에서 멈춘 위치에서부터 다시 실행을 재개 함.
//멈출 때는 호출자로 돌아가는 대신 돌아갈 위치를 지정할 수 있음.
//http:/blog.alexnesterov.com/post/coroutines/


//서브루틴이란?
//일반적으로 어떠한 function 을 실행시킨다면, 블락킹한다. 그렇기 때문에 1개의 진입점을 갖는다고 본다.
//코루틴은 앞서 여러개의 진입점을 가진다고 얘기했듯이, 외부 코드 블록을 실행시킬 수 있게 하고 그 이후에 다시 시작되도록 할 수 있다는 것.
//이것은 코루틴의 매개변수와 자동변수가 외부 코드 블록이 실행될때마다 코드블록에 보존되고 필요에 따라 복원된다는 것을 의미.
//결과적으로 싱글 스레드의 한계를 극복 or 성능에 효과적..
//https://stackoverflow.com/questions/24780935/difference-between-subroutine-co-routine-function-and-thread

//코루틴은 왜 나왔을까?
//쓰레드보다 coroutine 이 성능쪽에서 훨씬 나음. 메모리 사용면에서 적기 때문에 cpu도 더 적게 소비함. (gc..) (보통 1쓰레드가 1메가바이트 이상 필요)
//최근 뜨는 리액티브 프로그래밍보다 사용성 측면도 더 낫다는 의견



//Access토큰을 가져와서 글을 올리는 로직을 생각해보자.

//original synchronous
fun postItem1(item: Item) {
    val token = requestToken()
    val post = createPost(token, item)
    processPost(post)
}

//스레드 skip

////callback
fun postItem2(item: Item) {
    requestTokenAsync{ token ->     //함수는 () 밖으로 뺄 수 있
        createPostAsync(token, item) { post ->
            processPost(post)
        }
    }
}

////Future
//callback 보다 훨씬 깔끔한 코드.
//하지만 예외처리나 복잡한 combinator 가 생기면 처리하기가 어렵고 복잡해짐 (체이닝)
fun postItem3(item: Item) {
    requestTokenAsync2()
            .thenCompose{ token -> createPostAsync2(token, item)}
            .thenAccept{ post -> processPost(post)}
}

////reactive 도 비슷
fun postItem31(item: Item) {
    requestTokenAsyncRx()
            .map{ token -> createPost(token, item) }
            .map{ post -> processPost(post) }
            .subscribe()
}

////Coroutine
//코루틴은 regular code. 단지 suspend 키워드만 추가.
//코루틴을 생성해야하는 부분이 있지만 그래도 평범한 코드로 개발. (비동기식 코드를 동기식 코드처럼 개발)
//예외처리 & if & loop etc... 일반 코드처럼 가능. 코틀린에서 지원하는 고차함수도 어떤것이든 다 적용 가능.
//suspend 함수는 코루틴이나 suspend 함수 안에서만 호출 가능.
suspend fun postItem4(item: Item) {
    val token = requestTokenLaunch()
    val post = createPostForCoroutine(token, item)
    processPostForCoroutine(post)
}


//실제 바이트코드로 빌드된 것을 보면
//컴파일러에 의해 Continuation 객체로 담겨 이동... (일시 중단을 처리하고 유지할 수 있는 매개변수로 추가된 것)
//그리고 Continuation 객체를 코루틴 컨텍스트에서 내부적으로 스케쥴러 객체에 담고 사용하는데.. 결국 이벤트 루프!





//코루틴 생성을 어떻게 하는지 살펴보자.

//launch{}는 Thread{} 라고 생각하자. delay 가 에러나는 이유는 코루틴에서 지원하는 non-blocking이라서. (suspend 코루틴은 코루틴에서만 사용가능)
fun coroutinBasic() {
    launch { //새로운 코루틴 생성 (블록내 코드는 메인스레드가 아닌 백그라운드에서 돌아감. forkJoinCommponPool, default)
        delay(1000L) //논블록킹 delay for 1 second
        println("World!") //백그라운드 pool 의 스레드가 출력.
    }
    println("Hello,") //메인 스레드가 출력
    Thread.sleep(2000L) //process 종료를 막기 위해.
}


//2번예제 생략
//위와의 차이점은 메인스레드가 block되고, 밑은 commonPool의 스레드 1개가 block됨.
fun coroutinBasic2() {
    launch { //새로운 코루틴 생성
        delay(1000L)
        println("World!")
    }
    println("Hello,") // 메인 스레드가 출력
    runBlocking {   //메인스레드 블록킹
        delay(2000L)
    }
}



//실행끝을 기다리기위해 blocking 하는건 효율이 좋지 않다. job이 완료되길 기다리자
fun coroutinBasic3() = runBlocking {    //현재 스레드를 블록킹하여 코루틴을 실행
    val job = launch {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    job.join() // 코루틴 종료될때까지 기다림.
}

//{} 블록을 함수로 추출해보자. 첫 suspending function.
// 일반적인 함수처럼 보이지만, 실행을 중단할 수도 있고, 다른 suspending function을 사용할 수도 있다.. (일시 중지처럼 보이는)
//runBlocking 대신 launch를 사용해도 된다. (지금은 단지 테스트코드가 종료되지 않아야하기 때문에 블록킹함)
fun coroutinBasic4() = runBlocking {
    val job = launch { doWorld() }
    println("Hello,")
    job.join()
}
// this is your first suspending function
suspend fun doWorld() {
    delay(1000L)
    println("World!")
}


//코루틴은 가볍다!
//코루틴 10만개 돌려도 문제없음. 쓰레드는 뻗음 (OOM)
//코루틴의 메커니즘상 쓰레드와 다르게 생성과 ~?~에 부담이 없음.
fun coroutinBasic5() = runBlocking {
    val jobs = List(100_000) { //0부터 10만까지의 int list
        //1초 기다리고, '.' 출력하는 코루틴 실행
        launch {
            delay(1000L)
            print(".")
        }
    }
    jobs.forEach { it.join() } //모든 job을 기다림.
}

//6번도 삭제할것.
//active한 코루틴은 프로세스 유지하지 않는다.  (계속 실행 유지시키고 싶으면 코루틴 job 변수 선언후, job.join 하면 된다.
fun coroutinBasic6() = runBlocking {
    launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // just quit after delay
}

//코루틴 취소.
fun coroutinBasic7() = runBlocking<Unit> {
    val job = launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancel() // cancels the job
    job.join() // waits for job's completion
    println("main: Now I can quit.")
}



//이 외에도 아주 많은 기능이 제공..
//async : launch와 동일하지만 값 전달 (launch는 job을 반환하고 값은 전달하지 않음)






fun main(args: Array<String>) {
    //coroutinBasic()
    //coroutinBasic2()
    //coroutinBasic3()
    //coroutinBasic4()
    //coroutinBasic5()
    //coroutinBasic6()
    //coroutinBasic7()
    //https://github.com/Kotlin/kotlinx.coroutines/blob/379f210f1d6f6ee91d6198348bd16306c93153ce/coroutines-guide.md
}





//test functions For build
data class Item(val name: String)
fun requestToken() = ""
fun requestTokenAsyncRx(): Flux<String> = listOf("").toFlux()
fun createPost(token: String, item: Item) = token + item.name

//fun createPostForRx(token: String, item: Item) = Flux.just(token + item.name)
fun processPost(str: String) = ""
fun requestTokenAsync(function: (token: String) -> String): String {
    return ""
}
fun createPostAsync(token: String, item: Item, function: (token: String) -> String): String {
    return ""
}
fun requestTokenAsync2(): CompletableFuture<String> {
    return CompletableFuture.completedFuture("")
}
fun createPostAsync2(token: String?, item: Item): CompletableFuture<String> {
    return CompletableFuture.completedFuture("")
}


suspend fun requestTokenLaunch() = async{ "" }  //실제 suspend 되는 작업이 없어서 suspend는 빼도 되지만.. 예시를 위해.
suspend fun createPostForCoroutine(token: Deferred<String>, item: Item) = async{ token.await() + item.name }    //await : non-blocking
suspend fun processPostForCoroutine(str: Deferred<String>) = async{ str.await() }