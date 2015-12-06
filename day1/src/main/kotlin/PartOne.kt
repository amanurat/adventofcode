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
            .sum()
            .subscribe { println(it) }
}