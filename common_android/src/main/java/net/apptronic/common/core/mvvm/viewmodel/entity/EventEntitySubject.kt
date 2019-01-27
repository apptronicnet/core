package net.apptronic.common.core.mvvm.viewmodel.entity

import net.apptronic.common.core.mvvm.viewmodel.lifecycle.LifecycleHolder
import java.util.*

class EventEntitySubject<T>(private val lifecycleHolder: LifecycleHolder) :
    ViewModelEntitySubject<T> {

    private val callbacks = LinkedList<(T) -> Unit>()

    override fun send(value: T) {
        lifecycleHolder.getLifecycle().provideThreadExecutor().execute {
            callbacks.toTypedArray().forEach {
                it(value)
            }
        }
    }

    override fun subscribe(callback: (T) -> Unit): ViewModelEntitySubject.Subscription {
        callbacks.add(callback)
        return Sub(callback)
    }

    private inner class Sub(val callback: (T) -> Unit) : ViewModelEntitySubject.Subscription {
        override fun unsubscribe() {
            callbacks.remove(callback)
        }
    }

}