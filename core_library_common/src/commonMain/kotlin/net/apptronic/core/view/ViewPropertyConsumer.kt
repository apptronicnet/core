package net.apptronic.core.view

import net.apptronic.core.component.context.Context

interface ViewPropertyConsumer {

    val context: Context

    fun <T> ViewProperty<T>.subscribe(callback: (T) -> Unit) {
        subscribeWith(this@ViewPropertyConsumer.context, callback)
    }

}