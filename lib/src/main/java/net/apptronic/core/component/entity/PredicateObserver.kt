package net.apptronic.core.component.entity

interface PredicateObserver<T> {

    fun notify(value: T)

}