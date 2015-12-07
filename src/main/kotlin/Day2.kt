import extensions.lines
import extensions.minimumOf
import extensions.sum
import rx.Observable
import java.io.File

fun day2() {
    println("Day 2")

    val file = File("input/day2.txt")
    print("1: "); partOne(file)
    print("2: "); partTwo(file)
}

private fun partOne(input: File) {
    input.lines()
            .toDimensions()
            .map {
                it.run {
                    area() + minimumOf(length * width, width * height, height * length)
                }
            }
            .sum()
            .subscribe { println(it) }
}

private fun partTwo(input: File) {
    input.lines()
            .toDimensions()
            .map {
                it.run {
                    volume() + minimumOf(2 * (length + width), 2 * (length + height), 2 * (width + height))
                }
            }
            .sum()
            .subscribe { println(it) }
}

private fun Observable<String>.toDimensions(): Observable<Dimensions> {
    return map {
        Dimensions(
                length = it.substring(0, it.indexOf('x')).toInt(),
                width = it.substring(it.indexOf('x') + 1, it.lastIndexOf('x')).toInt(),
                height = it.substring(it.lastIndexOf('x') + 1).toInt()
        )
    }
}

data class Dimensions(val length: Int, val width: Int, val height: Int) {

    fun area() = 2 * length * width + 2 * width * height + 2 * height * length

    fun volume() = length * width * height
}