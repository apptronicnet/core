package net.apptronic.core.component.lifecycle

import net.apptronic.core.base.concurrent.requireNeverFrozen

internal class CompositeCallback {

    init {
        requireNeverFrozen()
    }

    private val innerCallbacks = mutableListOf<EventCallback>()

    fun execute() {
        val executed = mutableListOf<EventCallback>()
        do {
            val toExecute = innerCallbacks.filterNot {
                executed.contains(it)
            }
            toExecute.forEach { it.execute() }
            executed.addAll(toExecute)
        } while (innerCallbacks.size > executed.size)
    }

    internal fun add(callback: EventCallback) {
        callback.parent = this
        innerCallbacks.add(callback)
    }

    internal fun cancel(callback: EventCallback) {
        innerCallbacks.remove(callback)
    }

    internal fun clear() {
        innerCallbacks.clear()
    }

}