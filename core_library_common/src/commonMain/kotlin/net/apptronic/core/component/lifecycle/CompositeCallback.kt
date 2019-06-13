package net.apptronic.core.component.lifecycle

import net.apptronic.core.base.concurrent.requireNeverFrozen
import kotlin.native.concurrent.ThreadLocal

internal class CompositeCallback {

    init {
        requireNeverFrozen()
    }

    @ThreadLocal
    private val innerCallbacks = mutableListOf<EventCallback>()

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