import extensions.lines
import rx.Observable
import java.io.File
import java.security.MessageDigest
import javax.xml.bind.annotation.adapters.HexBinaryAdapter

fun day4() {
    println("Day 4")

    val file = File("input/day4.txt")
    print("1: "); partOne(file)
    print("2: "); partTwo(file)
}

private fun partOne(input: File) {
    findWhere(input) { startsWith("00000") }
}

private fun partTwo(input: File) {
    findWhere(input) { startsWith("000000") }
}

private fun findWhere(input: File, predicate: String.() -> Boolean) {
    input.lines()
            .flatMap { input ->
                Observable.range(0, Int.MAX_VALUE)
                        .map { input to "$it" }
            }
            .md5()
            .skipWhile { !it.second.predicate() }
            .first()
            .subscribe { println(it.first) }
}

private fun Observable<Pair<String, String>>.md5(): Observable<Pair<String, String>> {
    val md5 = MessageDigest.getInstance("MD5")

    return map {
        it.second to (HexBinaryAdapter().marshal(md5.digest("${it.first}${it.second}".toByteArray())))
    }
}
