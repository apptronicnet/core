package net.apptronic.common.android.ui.viewmodel.entity

import net.apptronic.common.android.ui.threading.ThreadExecutor
import java.util.*

class EventEntitySubject<T>(private val threadExecutor: ThreadExecutor) : ViewModelEntitySubject<T> {

    private val callbacks = LinkedList<(T) -> Unit>()

    override fun send(value: T) {
        threadExecutor.execute {
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