import extensions.lines
import extensions.sum
import java.io.File
import kotlin.text.Regex

fun main(args: Array<String>) {
    day8()
}

fun day8() {
    println("Day 8")

    val file = File("input/day8.txt")
    println("1: ${partOne(file)}")
    println("2: ${partTwo(file)}")
}

private fun partOne(input: File): Int {
    return input.lines()
            .map { it to unescape(it) }
            .map { it.first.length - it.second.length }
            .sum()
            .toBlocking().first()
}

private fun partTwo(input: File): Int {
    return input.lines()
            .map { it to escape(it) }
            .map { it.second.length - it.first.length }
            .sum()
            .toBlocking().first()
}

private fun unescape(s: String): String {
    return s
            .replace(Regex("\\\\\\\\"), "\\\\")
            .replace(Regex("\\\\x([0-9a-f]{2})"), "A")
            .replace(Regex("\""), "")
            .replace(Regex("\\\\\""), "\"")
}

private fun escape(s: String): String {
    return """"${s.replace("\\", "\\\\").replace("\"", "\\\"")}""""
}

