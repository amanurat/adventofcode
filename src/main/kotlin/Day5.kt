import extensions.lines
import java.io.File

fun day5() {
    println("Day 5")

    val file = File("input/day5.txt")
    print("1: "); partOne(file)
    print("2: "); partTwo(file)
}

private fun partOne(input: File) {
    countNiceWords(input) {
        containsThreeVowels() && containsDoubleLetters() && !containsForbiddenStrings()
    }
}

private fun partTwo(input: File) {
    countNiceWords(input) {
        containsPairTwice() && containsLetterTwiceWithOneLetterInBetween()
    }
}

private fun countNiceWords(input: File, predicate: String.() -> Boolean) {
    input.lines()
            .filter { it.predicate() }
            .count()
            .subscribe { println(it) }
}

private fun String.containsThreeVowels(): Boolean {
    var counter = 0

    for (c in this) {
        if (c in arrayOf('a', 'e', 'i', 'o', 'u')) {
            counter++
        }
    }

    return counter >= 3
}

private fun String.containsDoubleLetters(): Boolean {
    var previous: Char = 'A'
    for (c in this) {
        if (previous == c) {
            return true
        }
        previous = c
    }
    return false
}

private fun String.containsForbiddenStrings(): Boolean {
    return contains("ab") || contains("cd") || contains("pq") || contains("xy")
}

private fun String.containsPairTwice(): Boolean {
    for (i in 0..(length-2)) {
        if (substring(i + 2).contains("${get(i)}${get(i + 1)}")) {
            return true;
        }
    }
    return false
}

private fun String.containsLetterTwiceWithOneLetterInBetween(): Boolean {
    for (i in 0..(length - 3)) {
        if (get(i + 2) == get(i)) {
            return true
        }
    }
    return false
}

