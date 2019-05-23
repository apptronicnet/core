package net.apptronic.core.base.observable

import net.apptronic.core.base.AtomicReference
import net.apptronic.core.base.observable.subject.ValueHolder

fun <T> Observable<T>.subscribe(callback: (T) -> Unit): Subscription {
    return subscribe(object : Observer<T> {
        override fun notify(value: T) {
            callback.invoke(value)
        }
    })
}

fun <T> Observable<T>.distinctUntilChanged(): Observable<T> {
    return DistinctUntilChangedObservable(this)
}

private class DistinctUntilChangedObservable<T>(
        private val source: Observable<T>
) : Observable<T> {

    override fun subscribe(observer: Observer<T>): Subscription {
        return source.subscribe(DistinctUntilChangedObserver(observer))
    }

    private class DistinctUntilChangedObserver<T>(
            private val target: Observer<T>
    ) : Observer<T> {

        private var lastValue = AtomicReference<ValueHolder<T>?>(null)

        override fun notify(value: T) {
            val last = this.lastValue.get()
            if (last == null || last.value != value) {
                this.lastValue.set(ValueHolder(value))
                target.notify(value)
            }
        }

    }

}