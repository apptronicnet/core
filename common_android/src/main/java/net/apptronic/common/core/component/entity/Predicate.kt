package net.apptronic.common.core.component.entity

/**
 * Predicate is basic subscribtion provider
 */
interface Predicate<T> {

    fun subscribe(observer: (T) -> Unit): Subscription

}