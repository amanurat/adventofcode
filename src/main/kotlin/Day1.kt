import extensions.SubscriberAdapter
import extensions.chars
import extensions.sum
import rx.Observable
import java.io.File

fun day1() {
    println("Day 1")

    val file = File("input/day1.txt")
    print("1: "); partOne(file)
    print("2: "); partTwo(file)
}

private fun partOne(input: File) {
    input.chars()
            .map {
                when (it) {
                    '(' -> 1
                    ')' -> -1
                    else -> throw Error()
                }
            }
            .sum()
            .subscribe { println(it) }
}

private fun partTwo(input: File) {
    input.chars()
            .map {
                when (it) {
                    '(' -> 1
                    ')' -> -1
                    else -> throw Error()
                }
            }
            .lift(Observable.Operator<Pair<Int, Int>, Int> {
                object : SubscriberAdapter<Int>(it) {

                    private var counter = 0
                    private var sum = 0

                    override fun onNext(t: Int) {
                        counter++
                        sum += t

                        it.onNext(sum to counter)
                    }
                }
            })
            .takeUntil { it.first == -1 }
            .last()
            .subscribe { println(it.second) }
}