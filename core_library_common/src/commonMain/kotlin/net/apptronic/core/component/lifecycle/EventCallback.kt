package net.apptronic.core.component.lifecycle

internal class EventCallback(private val action: () -> Unit) {

    var parent: CompositeCallback? = null

    fun execute() {
        action()
    }

    fun cancel() {
        parent?.cancel(this)
    }

}