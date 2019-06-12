package net.apptronic.core.component.lifecycle

import net.apptronic.core.base.concurrent.AtomicReference
import kotlin.native.concurrent.SharedImmutable

internal class EventCallback(private val action: () -> Unit) {

    @SharedImmutable
    val parent = AtomicReference<CompositeCallback?>(null)

    fun execute() {
        action()
    }

    fun cancel() {
        parent.get()?.cancel(this)
    }

}