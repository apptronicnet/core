package net.apptronic.common.core.mvvm.viewmodel.entity

interface ViewModelEntitySubject<T> {

    interface Subscription {

        fun unsubscribe()

    }

    fun send(value: T)

    fun subscribe(callback: (T) -> Unit): Subscription

}