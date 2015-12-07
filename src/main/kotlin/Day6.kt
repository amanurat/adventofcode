import extensions.lines
import extensions.sum
import rx.Observable
import java.io.File

fun day6() {
    println("Day 6")

    val file = File("input/day6.txt")
    print("1: "); partOne(file)
    print("2: "); partTwo(file)
}

private fun partOne(input: File) {
    val grid = Array(1000, { BooleanArray(1000) })

    input.lines()
            .map { it.toInstruction() }
            .explode()
            .subscribe {
                when (it.type) {
                    Type.ON -> grid[it.x][it.y] = true
                    Type.OFF -> grid[it.x][it.y] = false
                    Type.TOGGLE -> grid[it.x][it.y] = !grid[it.x][it.y]
                }
            }

    grid(0 to 0, 999 to 999)
            .map { grid[it.first][it.second] }
            .filter { it == true }
            .count()
            .subscribe { println(it) }
}

private fun partTwo(input: File) {
    val grid = Array(1000, { IntArray(1000) })

    input.lines()
            .map { it.toInstruction() }
            .explode()
            .subscribe {
                when (it.type) {
                    Type.ON -> grid[it.x][it.y]++
                    Type.OFF -> grid[it.x][it.y] = Math.max(0, grid[it.x][it.y] - 1)
                    Type.TOGGLE -> grid[it.x][it.y] += 2
                }
            }

    grid(0 to 0, 999 to 999)
            .map { grid[it.first][it.second] }
            .sum()
            .subscribe { println(it) }
}

private fun Observable<Instruction>.explode(): Observable<SingleInstruction> {
    return flatMap { instruction ->
        grid(instruction.topLeft, instruction.bottomRight)
                .map {
                    SingleInstruction(
                            type = instruction.type,
                            x = it.first,
                            y = it.second
                    )
                }
    }
}

private fun grid(topLeft: Pair<Int, Int>, bottomRight: Pair<Int, Int>): Observable<Pair<Int, Int>> {
    return Observable.range(topLeft.first, bottomRight.first - topLeft.first + 1)
            .flatMap { x ->
                Observable.range(topLeft.second, bottomRight.second - topLeft.second + 1)
                        .map { x to it }
            }
}

private fun String.toInstruction(): Instruction {
    val split = replace("turn o", "turn_o").split(' ', ',')
    return Instruction(
            type = split[0].toType(),
            topLeft = split[1].toInt() to split[2].toInt(),
            bottomRight = split[4].toInt() to split[5].toInt()
    )
}

private fun String.toType(): Type {
    return when (this) {
        "turn_on" -> Type.ON
        "turn_off" -> Type.OFF
        else -> Type.TOGGLE
    }
}

private data class SingleInstruction(val type: Type, val x: Int, val y: Int)

private data class Instruction(val type: Type, val topLeft: Pair<Int, Int>, val bottomRight: Pair<Int, Int>)

private enum class Type { ON, OFF, TOGGLE }

