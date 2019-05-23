package net.apptronic.core.component.lifecycle

import net.apptronic.core.platform.AtomicReference

internal class EventCallback(private val action: () -> Unit) {

    val parent = AtomicReference<CompositeCallback?>(null)

    fun execute() {
        action()
    }

    fun cancel() {
        parent.get()?.cancel(this)
    }

}