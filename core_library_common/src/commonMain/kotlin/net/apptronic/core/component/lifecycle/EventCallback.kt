package net.apptronic.core.component.lifecycle

import net.apptronic.core.base.concurrent.Volatile

internal class EventCallback(private val action: () -> Unit) {

    val parent = Volatile<CompositeCallback?>(null)

    fun execute() {
        action()
    }

    fun cancel() {
        parent.get()?.cancel(this)
    }

}