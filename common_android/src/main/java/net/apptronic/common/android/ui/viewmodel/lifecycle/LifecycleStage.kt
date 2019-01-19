package net.apptronic.common.android.ui.viewmodel.lifecycle

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.collections.HashMap

class LifecycleStage(val lifecycle: Lifecycle, val name: String) {

    private val isEntered = AtomicBoolean(false)

    private val enterCallback = CompositeCallback()
    private val exitCallback = CompositeCallback()
    private val whenEnteredActions = HashMap<String, (() -> Unit)>()

    private val enterHandler = AtomicReference<OnEnterHandlerImpl>()
    private val exitHandler = AtomicReference<OnExitHandlerImpl>()

    override fun toString(): String {
        return super.toString() + " $name isEntered=${isEntered.get()}"
    }

    /**
     * This callbacks are internally created to be executed on exit stage command
     */
    private val inStageCallbacks = LinkedList<EventCallback>()

    fun isEntered(): Boolean = isEntered.get()

    fun enter() {
        if (lifecycle.isTerminated()) {
            return
        }
        if (isEntered()) {
            throw IllegalStateException("Stage already entered")
        }
        enterHandler.set(OnEnterHandlerImpl())
        exitHandler.set(OnExitHandlerImpl())
        isEntered.set(true)
        enterCallback.execute()
        whenEnteredActions.values.forEach { it.invoke() }
        whenEnteredActions.clear()
    }

    fun exit() {
        if (lifecycle.isTerminated()) {
            return
        }
        if (!isEntered()) {
            throw IllegalStateException("Stage already exited")
        }
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

    /**
     * This function is used to perform single-shot action when lifecycle stage will be entered.
     * It is lifecycle-independent and will not cancelled also if current stage will exit. If
     * current lifecycle stage already entered - action will be executed immediately.
     * It may be useful
     *
     * @param key unique key for action. If action with same key already exists - previous action
     * will be disposed
     * @param action Action to perform when stage will be entered
     */
    fun doOnce(key: String, action: () -> Unit) {
        if (lifecycle.isTerminated()) {
            return
        }
        if (isEntered()) {
            action()
        } else {
            whenEnteredActions[key] = action
        }
    }

    /**
     * Subscribe to
     */
    fun doOnEnter(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        if (lifecycle.isTerminated()) {
            return
        }
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
        if (lifecycle.isTerminated()) {
            return
        }
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


    private fun cancelOnExit(callback: EventCallback) {
        inStageCallbacks.add(callback)
    }

    interface OnEnterHandler {

        fun Disposable.disposeOnExit()

        fun onExit(callback: OnExitHandler.() -> Unit)

    }

    interface OnExitHandler {

    }

}