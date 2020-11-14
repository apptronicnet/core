package net.apptronic.core.base.observable

interface Observer<T> {

    fun update(value: T)

}