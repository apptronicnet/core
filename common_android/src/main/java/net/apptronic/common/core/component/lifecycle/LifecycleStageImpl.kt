package net.apptronic.common.core.component.lifecycle

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import net.apptronic.common.core.base.AtomicEntity
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.collections.HashMap

internal class LifecycleStageImpl(val parent: LifecycleStageParent, val name: String) :
    LifecycleStage, LifecycleStageParent {

    private val childStage = AtomicEntity<LifecycleStageImpl?>(null)

    private val isEntered = AtomicBoolean(false)
    private val isTerminated = AtomicBoolean(false)

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

    fun terminate() {
        childStage.perform {
            get()?.terminate()
            set(null)
        }
        if (isEntered.get()) {
            exit()
        }
        isTerminated.set(true)
    }

    override fun onChildEnter() {
        if (!isEntered.get()) {
            enter()
        }
    }

    fun last(): LifecycleStageImpl {
        return childStage.get() ?: this
    }

    fun lastEntered(): LifecycleStageImpl? {
        return if (isEntered.get()) {
            childStage.get()?.lastEntered() ?: this
        } else null
    }

    fun stageByName(name: String): LifecycleStageImpl? {
        return if (name == this.name) {
            this
        } else {
            childStage.get()?.stageByName(name)
        }
    }

    override fun addStage(name: String): LifecycleStage {
        return childStage.perform {
            get()?.terminate()
            LifecycleStageImpl(this@LifecycleStageImpl, name).also {
                set(it)
            }
        }
    }

    internal fun enter() {
        if (isTerminated.get() || isEntered.get()) {
            return
        }
        parent.onChildEnter()
        enterHandler.set(OnEnterHandlerImpl())
        exitHandler.set(OnExitHandlerImpl())
        isEntered.set(true)
        enterCallback.execute()
        whenEnteredActions.values.forEach { it.invoke() }
        whenEnteredActions.clear()
    }

    internal fun exit() {
        if (isTerminated.get() || !isEntered.get()) {
            return
        }
        childStage.get()?.exit()
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

    override fun doOnce(action: () -> Unit) {
        doOnce("", action)
    }

    override fun doOnce(key: String, action: () -> Unit) {
        if (isTerminated.get()) {
            return
        }
        if (isEntered.get()) {
            action()
        } else {
            whenEnteredActions[key] = action
        }
    }

    /**
     * Subscribe to
     */
    override fun doOnEnter(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        if (!isEntered.get()) {
            return
        }
        if (isTerminated.get()) {
            return
        }
        val eventCallback = subscribeEnter(callback)
        enterCallback.add(eventCallback)
        parent.cancelOnExitFromActiveStage(eventCallback)
    }

    private fun subscribeExit(callback: LifecycleStage.OnExitHandler.() -> Unit): EventCallback {
        return EventCallback {
            exitHandler.get()?.callback()
        }
    }

    override fun doOnExit(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        if (!isEntered.get() || isTerminated.get()) {
            return
        }
        val eventCallback = subscribeExit(callback)
        exitCallback.add(eventCallback)
        parent.cancelOnExitFromActiveStage(eventCallback)
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

    override fun cancelOnExitFromActiveStage(eventCallback: EventCallback) {
        if (isEntered.get()) {
            cancelOnExit(eventCallback)
        } else {
            parent.cancelOnExitFromActiveStage(eventCallback)
        }
    }

}