package net.apptronic.common.core.mvvm.viewmodel.entity

import net.apptronic.common.core.mvvm.viewmodel.lifecycle.LifecycleHolder
import java.util.*

class ValueEntitySubject<T>(private val lifecycleHolder: LifecycleHolder) :
    ViewModelEntitySubject<T> {

    private val lock = Any()

    private val callbacks = LinkedList<(T) -> Unit>()

    @Volatile
    private var valueHolder: ValueHolder<T>? = null

    override fun send(value: T) {
        lifecycleHolder.getLifecycle().provideThreadExecutor().execute {
            synchronized(lock) {
                val valueHolder = this.valueHolder
                if (valueHolder == null || valueHolder.value != value) {
                    this.valueHolder = ValueHolder(value)
                    callbacks.toTypedArray().forEach {
                        it(value)
                    }
                }
            }
        }
    }

    override fun subscribe(callback: (T) -> Unit): ViewModelEntitySubject.Subscription {
        lifecycleHolder.getLifecycle().provideThreadExecutor().execute {
            callbacks.add(callback)
            valueHolder?.let {
                callback(it.value)
            }
        }
        return ThisSubscription(callback)
    }

    private inner class ThisSubscription(val callback: (T) -> Unit) : ViewModelEntitySubject.Subscription {
        override fun unsubscribe() {
            lifecycleHolder.getLifecycle().provideThreadExecutor().execute {
                synchronized(lock) {
                    callbacks.remove(callback)
                }
            }
        }
    }

}