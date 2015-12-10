package extensions

import rx.Observable
import rx.Subscriber

fun Observable<Int>.sum() = lift<Int>(
        Observable.Operator<Int, Int> {
            object : SubscriberAdapter<Int>(it) {
                private var count = 0

                override fun onNext(t: Int) {
                    count += t
                }

                override fun onCompleted() {
                    it.onNext(count)
                    it.onCompleted()
                }
            }
        }
)

fun <T> Observable<T>.skipOnIndex(predicate: (Int) -> Boolean): Observable<T> {
    return lift(Observable.Operator {
        object : SubscriberAdapter<T>(it) {

            private var count = 0

            override fun onNext(t: T) {
                if (!predicate(count)) {
                    it.onNext(t)
                }
                count++;
            }
        }
    })
}

fun <T> Observable<T>.printIt() {
    subscribe { println(it) }
}

abstract class SubscriberAdapter<T>(val s: Subscriber<*>) : Subscriber<T>() {

    override fun onError(e: Throwable) {
        s.onError(e)
    }

    override fun onCompleted() {
        s.onCompleted()
    }
}
