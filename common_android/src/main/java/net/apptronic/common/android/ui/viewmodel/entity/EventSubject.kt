package net.apptronic.common.android.ui.viewmodel.entity

import net.apptronic.common.android.ui.threading.ThreadExecutor
import java.util.*

class EventSubject<T>(private val threadExecutor: ThreadExecutor) : ViewModelSubject<T> {

    private val callbacks = LinkedList<(T) -> Unit>()

    override fun send(value: T) {
        threadExecutor.execute {
            callbacks.toTypedArray().forEach {
                it(value)
            }
        }
    }

    override fun subscribe(callback: (T) -> Unit): ViewModelSubject.Subscription {
        callbacks.add(callback)
        return Sub(callback)
    }

    private inner class Sub(val callback: (T) -> Unit) : ViewModelSubject.Subscription {
        override fun unsubscribe() {
            callbacks.remove(callback)
        }
    }

}