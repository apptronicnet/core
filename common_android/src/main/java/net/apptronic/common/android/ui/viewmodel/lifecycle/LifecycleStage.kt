package net.apptronic.common.android.ui.viewmodel.lifecycle

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class LifecycleStage(val lifecycle: Lifecycle, val name: String) {

    private val isEntered = AtomicBoolean(false)

    private val enterCallback = CompositeCallback()
    private val exitCallback = CompositeCallback()

    private var enterHandler = AtomicReference<OnEnterHandlerImpl>()
    private val exitHandler = AtomicReference<OnExitHandlerImpl>()

    private val inStageCallbacks = LinkedList<EventCallback>()

    fun isEntered(): Boolean = isEntered.get()

    fun enter() {
        enterHandler.set(OnEnterHandlerImpl())
        exitHandler.set(OnExitHandlerImpl())
        isEntered.set(true)
        enterCallback.execute()
    }

    fun exit() {
        isEntered.set(false)
        exitCallback.execute()
        exitHandler.get().disposables.dispose()
        inStageCallbacks.forEach { it.cancel() }
        inStageCallbacks.clear()
    }

    private fun subscribeEnter(callback: LifecycleStage.OnEnterHandler.() -> Unit): EventCallback {
        return EventCallback {
            enterHandler.get()?.callback()
        }.apply {
            if (isEntered.get()) {
                execute()
            }
        }
    }

    fun doOnEnter(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        val eventCallback = subscribeEnter(callback)
        enterCallback.add(eventCallback)
        lifecycle.getActiveStage().cancelOnExit(eventCallback)
    }

    private fun subscribeExit(callback: LifecycleStage.OnExitHandler.() -> Unit): EventCallback {
        return EventCallback {
            exitHandler.get()?.callback()
        }
    }

    fun doOnExit(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        val eventCallback = subscribeExit(callback)
        exitCallback.add(eventCallback)
        lifecycle.getActiveStage().cancelOnExit(eventCallback)
    }

    private inner class OnEnterHandlerImpl : LifecycleStage.OnEnterHandler {

        override fun Disposable.disposeOnExit() {
            exitHandler.get().disposables.add(this)
        }

        override fun onExit(callback: LifecycleStage.OnExitHandler.() -> Unit) {
            doOnExit(callback)
        }
    }

    private inner class OnExitHandlerImpl : LifecycleStage.OnExitHandler {

        val disposables = CompositeDisposable()

    }


    internal fun cancelOnExit(callback: EventCallback) {
        inStageCallbacks.add(callback)
    }

    interface OnEnterHandler {

        fun Disposable.disposeOnExit()

        fun onExit(callback: OnExitHandler.() -> Unit)

    }

    interface OnExitHandler {

    }

}