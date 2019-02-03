package net.apptronic.common.core.component.entity

import java.util.*

class MutableValueSubject<T>(private val lifecycleHolder: LifecycleHolder) :
    ViewModelEntitySubject<T> {

    private val lock = Any()

    private val callbacks = LinkedList<(T) -> Unit>()

    @Volatile
    private var valueHolder: ValueHolder<T>? = null

    override fun send(value: T) {
        lifecycleHolder.getLifecycle().provideThreadExecutor().execute {
            synchronized(lock) {
                this.valueHolder = ValueHolder(value)
                callbacks.toTypedArray().forEach {
                    it(value)
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

    private inner class ThisSubscription(val callback: (T) -> Unit) :
        ViewModelEntitySubject.Subscription {
        override fun unsubscribe() {
            lifecycleHolder.getLifecycle().provideThreadExecutor().execute {
                synchronized(lock) {
                    callbacks.remove(callback)
                }
            }
        }
    }

}