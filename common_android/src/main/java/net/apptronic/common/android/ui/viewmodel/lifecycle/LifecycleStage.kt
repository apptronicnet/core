package net.apptronic.common.android.ui.viewmodel.lifecycle

import java.util.concurrent.atomic.AtomicBoolean

class LifecycleStage(val name: String) {

    private val isEntered = AtomicBoolean(false)
    private val enter = LifecycleEvent()
    private val exit = LifecycleEvent()

    fun isEntered() = isEntered.get()

    fun enter() {
        isEntered.set(true)
        enter.notifyListeners()
    }

    fun exit() {
        isEntered.set(false)
        exit.notifyListeners()
    }

    override fun toString(): String {
        return "LifecycleStage: $name"
    }

    fun subscribeEnter(callback: () -> Unit) {
        subscribeEnter(object : LifecycleEvent.Listener {
            override fun onEvent(event: LifecycleEvent) {
                callback()
            }
        })
    }

    fun subscribeExit(callback: () -> Unit) {
        subscribeExit(object : LifecycleEvent.Listener {
            override fun onEvent(event: LifecycleEvent) {
                callback()
            }
        })
    }

    fun subscribeEnter(listener: LifecycleEvent.Listener) {
        enter.subscribe(listener)
        if (isEntered.get()) {
            listener.onEvent(enter)
        }
    }

    fun subscribeExit(listener: LifecycleEvent.Listener) {
        exit.subscribe(listener)
    }

}