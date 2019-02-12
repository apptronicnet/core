package net.apptronic.common.core.component.entity

interface Predicate<T> {

    fun subscribe(observer: (T) -> Unit): Subscription

}