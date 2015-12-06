import rx.Observable
import rx.Subscriber

fun Observable<Int>.sum() = lift<Int>(
        Observable.Operator<Int, Int> {
            object : Subscriber<Int>() {
                private var count = 0

                override fun onNext(t: Int) {
                    count += t
                }

                override fun onError(e: Throwable?) {
                    it.onError(e)
                }

                override fun onCompleted() {
                    it.onNext(count)
                    it.onCompleted()
                }
            }
        }
)
