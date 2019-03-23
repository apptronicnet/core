package net.apptronic.common.core.component.lifecycle

import java.util.*

internal class CompositeCallback {

    private val innerCallbacks = LinkedList<EventCallback>()

    fun execute() {
        innerCallbacks.forEach { it.execute() }
    }

    internal fun add(callback: EventCallback) {
        callback.parent.set(this)
        innerCallbacks.add(callback)
    }

    internal fun cancel(callback: EventCallback) {
        innerCallbacks.remove(callback)
    }

    internal fun clear() {
        innerCallbacks.clear()
    }

}