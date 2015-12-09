import extensions.lines
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    day9()
}

fun day9() {
    println("Day 9")

    val file = File("input/day9.txt")
    println("1: ${partOne(file)}")
    println("2: ${partTwo(file)}")
}

private fun partOne(input: File): Int {
    return find(input) { a, b -> Math.min(a, b ?: Int.MAX_VALUE) }
}

private fun partTwo(input: File): Int {
    return find(input) { a, b -> Math.max(a, b ?: Int.MIN_VALUE) }
}

private fun find(input: File, bestOf: (Int, Int?) -> Int): Int {
    return input.lines()
            .map { it.split(" ") }
            .collect(
                    { HashMap<String, MutableList<Distance>>() },
                    { map, line ->
                        map.getOrPut(line[0], { ArrayList() }).add(Distance(line[2], line[4].toInt()))
                        map.getOrPut(line[2], { ArrayList() }).add(Distance(line[0], line[4].toInt()))
                    }
            )
            .map {
                var best: Int? = null
                for (location in it.keys) {
                    val result = shortest(it[location]!!, it.without(location), bestOf)
                    best = bestOf(result, best)
                }
                best!!
            }.toBlocking().first()
}

private fun shortest(distances: List<Distance>, locations: Map<String, MutableList<Distance>>, bestOf: (Int, Int?) -> Int): Int {
    if (locations.isEmpty()) {
        return 0
    }

    var best: Int? = null
    for (distance in distances) {
        if (!locations.containsKey(distance.to)) {
            continue
        }

        var result = shortest(
                distances = locations[distance.to]!!,
                locations = locations.without(distance.to),
                bestOf = bestOf
        )

        best = bestOf(result + distance.distance, best)
    }

    return best!!
}

fun <T> Set<T>.without(t: T): Set<T> {
    val result = LinkedHashSet<T>(size)

    for (element in this) {
        if (element != t) {
            result.add(element)
        }
    }

    return result
}

fun <K, V> Map<K, V>.without(key: K): Map<K, V> {
    val result = HashMap<K, V>(size - 1)

    for (k in keys.without(key)) {
        result.put(k, get(k)!!)
    }

    return result
}

private data class Distance(val to: String, val distance: Int)

