package net.apptronic.common.core.component.lifecycle

import java.util.concurrent.atomic.AtomicReference

internal class EventCallback(private val action: () -> Unit) {

    val parent = AtomicReference<CompositeCallback>()

    fun execute() {
        action()
    }

    fun cancel() {
        parent.get()?.cancel(this)
    }

}