package net.apptronic.common.android.ui.viewmodel.entity

import net.apptronic.common.android.ui.threading.ThreadExecutor
import java.util.*

class ValueSubject<T>(private val threadExecutor: ThreadExecutor) : ViewModelSubject<T> {

    private val callbacks = LinkedList<(T?) -> Unit>()

    @Volatile
    private var valueHolder: ValueHolder<T?>? = null

    override fun send(value: T?) {
        threadExecutor.execute {
            synchronized(this) {
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

    override fun subscribe(callback: (T?) -> Unit): ViewModelSubject.Subscription {
        threadExecutor.execute {
            callbacks.add(callback)
            valueHolder?.let {
                callback(it.value)
            }
        }
        return Sub(callback)
    }

    private inner class Sub(val callback: (T?) -> Unit) : ViewModelSubject.Subscription {
        override fun unsubscribe() {
            callbacks.remove(callback)
        }
    }

}