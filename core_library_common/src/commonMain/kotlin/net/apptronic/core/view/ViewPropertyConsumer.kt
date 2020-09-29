package net.apptronic.core.view

import net.apptronic.core.component.context.Context

interface ViewPropertyConsumer {

    val context: Context

    fun <T> ViewProperty<T>.subscribe(callback: (T) -> Unit) {
        subscribeWith(this@ViewPropertyConsumer.context, callback)
    }

    fun <T1, T2> mergeProperties(
            p1: ViewProperty<T1>,
            p2: ViewProperty<T2>,
            callback: (T1, T2) -> Unit
    ) {
        p1.subscribe {
            callback(it, p2.getValue())
        }
        p2.subscribe {
            callback(p1.getValue(), it)
        }
    }

    fun <T1, T2, T3> mergeProperties(
            p1: ViewProperty<T1>,
            p2: ViewProperty<T2>,
            p3: ViewProperty<T3>,
            callback: (T1, T2, T3) -> Unit
    ) {
        p1.subscribe {
            callback(it, p2.getValue(), p3.getValue())
        }
        p2.subscribe {
            callback(p1.getValue(), it, p3.getValue())
        }
        p3.subscribe {
            callback(p1.getValue(), p2.getValue(), it)
        }
    }

}