package net.apptronic.core.component.lifecycle

internal class EventCallback(private val action: () -> Unit) {

    val parent = AtomicReference<CompositeCallback>()

    fun execute() {
        action()
    }

    fun cancel() {
        parent.get()?.cancel(this)
    }

}