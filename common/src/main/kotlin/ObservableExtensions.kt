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

abstract class SubscriberAdapter<T>(val s: Subscriber<*>) : Subscriber<T>() {

    override fun onError(e: Throwable) {
        s.onError(e)
    }

    override fun onCompleted() {
        s.onCompleted()
    }
}
