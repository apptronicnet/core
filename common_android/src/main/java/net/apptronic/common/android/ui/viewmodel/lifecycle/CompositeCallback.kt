package net.apptronic.common.android.ui.viewmodel.lifecycle

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

}