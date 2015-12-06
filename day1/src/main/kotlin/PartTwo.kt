import rx.Observable
import java.io.File

fun main(args: Array<String>) {
    File("day1/input.txt").chars()
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
            .subscribe { println(it) }
}
