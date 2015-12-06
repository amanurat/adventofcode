import rx.Observable
import java.io.File

fun File.lines(): Observable<String> {
    return Observable.from(readLines())
}

fun File.chars(): Observable<Char> {
    return lines().flatMap {
        Observable.from(it.chars())
    }
}