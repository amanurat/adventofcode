import extensions.chars
import rx.Observable
import rx.Subscriber

fun main(args: Array<String>) {
    day10()
}

fun day10() {
    println("Day 10")

    val start = System.currentTimeMillis()
    val input = "1113122113"
    val partOne = partOne(input)
    println("1: ${partOne.length}")
    println("2: ${partTwo(partOne).length}")
    println("Took: ${System.currentTimeMillis() - start}")
}

private fun partOne(input: String): String {
    var current = input

    for (i in 1..40)
        current = Observable.from(current.chars())
                .map { it.toString().toInt() }
                .lookAndSay()
                .concatenate()
                .toBlocking().first()

    return current
}

private fun partTwo(input: String): String {
    var current = input

    for (i in 1..10) {
        current = Observable.from(current.chars())
                .map { it.toString().toInt() }
                .lookAndSay()
                .concatenate()
                .toBlocking().first()
    }

    return current
}

private fun Observable<Int>.lookAndSay(): Observable<String> {
    return lift(Observable.Operator<String, Int> {
        object : Subscriber<Int>() {

            private var previous: Int? = null

            private var count = 0

            override fun onNext(t: Int) {
                if (previous != t) {
                    if (previous != null) {
                        it.onNext("$count$previous")
                    }

                    previous = null
                    count = 0
                }

                previous = t
                count++
            }

            override fun onError(e: Throwable) {
                it.onError(e)
            }

            override fun onCompleted() {
                if (previous != null) {
                    it.onNext("$count$previous")
                }
                it.onCompleted()
            }

        }
    })
}

private fun Observable<String>.concatenate(): Observable<String> {
    return lift(Observable.Operator<String, String> {
        object : Subscriber<String>() {

            private var result = StringBuilder()

            override fun onNext(t: String) {
                result.append(t)
            }

            override fun onError(e: Throwable) {
                it.onError(e)
            }

            override fun onCompleted() {
                it.onNext(result.toString())
                it.onCompleted()
            }
        }
    })
}

