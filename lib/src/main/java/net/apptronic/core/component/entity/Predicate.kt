package net.apptronic.core.component.entity

/**
 * Predicate is basic subscription provider
 */
interface Predicate<T> {

    fun subscribe(observer: PredicateObserver<T>): Subscription

}

