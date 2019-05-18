package net.apptronic.core.base.observable

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

        @Volatile
        private var oldValue: ValueHolder<T>? = null

        override fun notify(value: T) {
            val oldValue = this.oldValue
            if (oldValue == null || oldValue.value != value) {
                this.oldValue = ValueHolder(value)
                target.notify(value)
            }
        }

    }

}