package net.apptronic.core.component.entity

/**
 * Predicate is basic subscribtion provider
 */
interface Predicate<T> {

    fun subscribe(observer: (T) -> Unit): Subscription

}