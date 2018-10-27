package net.apptronic.common.android.ui.viewmodel.lifecycle

import java.util.*

class LifecycleEvent {

    interface Listener {

        fun onEvent(event: LifecycleEvent)

    }

    private val listeners = LinkedList<Listener>()

    fun notifyListeners() {
        listeners.toTypedArray().forEach {
            it.onEvent(this)
        }
    }

    fun subscribe(listener: Listener) {
        listeners.add(listener)
    }

    fun unsubscribe(listener: Listener) {
        listeners.remove(listener)
    }

}