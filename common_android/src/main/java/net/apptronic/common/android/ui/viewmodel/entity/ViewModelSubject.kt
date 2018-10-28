package net.apptronic.common.android.ui.viewmodel.entity

interface ViewModelSubject<T> {

    interface Subscription {

        fun unsubscribe()

    }

    fun send(value: T)

    fun subscribe(callback: (T) -> Unit): Subscription

}