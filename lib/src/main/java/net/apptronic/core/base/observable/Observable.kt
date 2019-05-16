package net.apptronic.core.base.observable

/**
 * [Observable] is basic subscription provider
 */
interface Observable<T> {

    fun subscribe(observer: Observer<T>): Subscription

}

