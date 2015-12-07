import rx.Observable
import java.io.File

fun main(args: Array<String>) {
    val file = File("day3/input.txt")
    partOne(file)
    partTwo(file)
}

fun partOne(input: File) {
    input.chars()
            .toCoordinates()
            .distinct()
            .count()
            .subscribe { println(it) }
}

fun partTwo(input: File) {
    Observable.merge(
            input.chars()
                    .skipOnIndex { it % 2 == 0 }
                    .toCoordinates(),
            input.chars()
                    .skipOnIndex { it % 2 != 0 }
                    .toCoordinates()
    )
            .distinct()
            .count()
            .subscribe { println(it) }
}

fun Observable<Char>.toCoordinates(): Observable<Pair<Int, Int>> {
    return lift(Observable.Operator<Pair<Int, Int>, Char> {
        object : SubscriberAdapter<Char>(it) {

            private var x = 0;
            private var y = 0;

            init {
                it.onNext(x to y)
            }

            override fun onNext(t: Char) {
                when (t) {
                    '^' -> y--
                    '>' -> x++
                    'v' -> y++
                    '<' -> x--
                }

                it.onNext(x to y)
            }
        }
    })
}