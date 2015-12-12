import extensions.chars
import extensions.lines
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    day11()
}

fun day11() {
    println("Day 11")

    val start = System.currentTimeMillis()
    var input = File("input/day11.txt").lines().toBlocking().first()
    val partOne = partOne(input)
    println("1: $partOne")
    println("2: ${partOne(partOne)}")
    println("Took: ${System.currentTimeMillis() - start}")
}

private fun <T, R> List<T>.mapReversed(function: (T) -> R): List<R> {
    val result = LinkedList<R>()

    for (t in this.reversed()) {
        result.add(0, function(t))
    }

    return result
}

private fun Char.toZeroInt(): Int {
    return toInt() - 'a'.toInt()
}

private fun partOne(input: String): String {
    var intValues = input.chars()
            .map { it.toZeroInt() }

    do {
        var carry = 1
        intValues = intValues
                .mapReversed {
                    if (carry == 0 ) {
                        it
                    } else {
                        var r = (it + carry)
                        if (r == 26) {
                            r %= 26
                        } else {
                            carry = 0
                        }
                        r
                    }
                }
    } while (!intValues.isValidPassword())

    return intValues
            .map { (it.toInt() + 'a'.toInt()).toChar() }
            .fold(StringBuilder()) { s, c -> s.append(c) }
            .toString()

}

private fun partTwo(input: String): String {
    return input
}

private fun List<Int>.isValidPassword(): Boolean {
    val containsForbiddenLetters = containsForbiddenLetters()
    return !containsForbiddenLetters && hasIncreasingLetters() && hasTwoPairs()
}

private fun List<Int>.containsForbiddenLetters(): Boolean {
    return contains('i'.toZeroInt()) || contains('o'.toZeroInt()) || contains('l'.toZeroInt())
}

private fun List<Int>.hasTwoPairs(): Boolean {
    var count = 0
    var previous: Int? = null
    var i = 0

    while ( i < size) {
        if (previous == null) {
            previous = get(i)
        } else if (previous == get(i)) {
            count++
            if (count == 2) {
                return true
            }

            previous = null
        } else {
            previous = get(i)
        }

        i++
    }

    return false
}

private fun List<Int>.hasIncreasingLetters(): Boolean {
    var count = 0
    var previous: Int? = null
    for (i in this) {
        if (previous == null) {
            count++
        } else if ((previous) == (i - 1)) {
            count++
        } else {
            count = 0
        }

        previous = i
        if (count == 2) {
            return true
        }
    }

    return false
}
