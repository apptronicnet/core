package net.apptronic.core.component.lifecycle

import net.apptronic.core.platform.getPlatform

internal class EventCallback(private val action: () -> Unit) {

    val parent = getPlatform().createAtomicReference<CompositeCallback?>(null)

    fun execute() {
        action()
    }

    fun cancel() {
        parent.get()?.cancel(this)
    }

}