package net.apptronic.core.base.observable

/**
 * [Observable] is basic subscription provider
 */
interface Observable<T> {

    fun subscribe(observer: Observer<T>): Subscription

    fun subscribe(callback: (T) -> Unit): Subscription {
        return subscribe(object : Observer<T> {
            override fun notify(value: T) {
                callback.invoke(value)
            }
        })
    }

}

