import java.io.File

fun main(args: Array<String>) {
    File("day2/input.txt").lines()
            .map {
                Dimensions(
                        length = it.substring(0, it.indexOf('x')).toInt(),
                        width = it.substring(it.indexOf('x') + 1, it.lastIndexOf('x')).toInt(),
                        height = it.substring(it.lastIndexOf('x') + 1).toInt()
                )
            }
            .map {
                it.run {
                    volume() + minimumOf(2 * (length + width), 2 * (length + height), 2 * (width + height))
                }
            }
            .sum()
            .subscribe { println(it) }
}
