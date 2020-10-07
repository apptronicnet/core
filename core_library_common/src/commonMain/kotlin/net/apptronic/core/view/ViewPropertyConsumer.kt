package net.apptronic.core.view

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context

@UnderDevelopment
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
            callback(it, p2.get())
        }
        p2.subscribe {
            callback(p1.get(), it)
        }
    }

    fun <T1, T2, T3> mergeProperties(
            p1: ViewProperty<T1>,
            p2: ViewProperty<T2>,
            p3: ViewProperty<T3>,
            callback: (T1, T2, T3) -> Unit
    ) {
        p1.subscribe {
            callback(it, p2.get(), p3.get())
        }
        p2.subscribe {
            callback(p1.get(), it, p3.get())
        }
        p3.subscribe {
            callback(p1.get(), p2.get(), it)
        }
    }

    fun <T1, T2, T3, T4> mergeProperties(
            p1: ViewProperty<T1>,
            p2: ViewProperty<T2>,
            p3: ViewProperty<T3>,
            p4: ViewProperty<T4>,
            callback: (T1, T2, T3, T4) -> Unit
    ) {
        p1.subscribe {
            callback(it, p2.get(), p3.get(), p4.get())
        }
        p2.subscribe {
            callback(p1.get(), it, p3.get(), p4.get())
        }
        p3.subscribe {
            callback(p1.get(), p2.get(), it, p4.get())
        }
        p4.subscribe {
            callback(p1.get(), p2.get(), p3.get(), it)
        }
    }

}