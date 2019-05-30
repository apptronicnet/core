package net.apptronic.core.component.lifecycle

import net.apptronic.core.base.concurrent.AtomicEntity
import net.apptronic.core.base.concurrent.AtomicReference
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscriptions.EntitySubscriptionListener

internal class LifecycleStageImpl(val parent: LifecycleStageParent, val name: String) :
        LifecycleStage, LifecycleStageParent, EntitySubscriptionListener {

    private val childStage = AtomicEntity<LifecycleStageImpl?>(null)

    private val isEntered = AtomicReference(false)
    private val isTerminated = AtomicReference(false)

    private val enterCallback = CompositeCallback()
    private val exitCallback = CompositeCallback()
    private val whenEnteredActions = HashMap<String, (() -> Unit)>()

    private val enterHandler = AtomicReference<OnEnterHandlerImpl?>(null)
    private val exitHandler = AtomicReference<OnExitHandlerImpl?>(null)
    private val subscriptions = mutableListOf<EntitySubscription>()

    override fun toString(): String {
        return super.toString() + " $name isEntered=${isEntered.get()}"
    }

    override fun onUnsubscribed(subscription: EntitySubscription) {
        subscriptions.remove(subscription)
    }

    override fun registerSubscription(subscription: EntitySubscription) {
        if (isEntered()) {
            subscriptions.add(subscription)
            subscription.registerListener(this)
        } else {
            subscription.unsubscribe()
        }
    }

    /**
     * This callbacks are internally created to be executed on exit stage command
     */
    private val inStageCallbacks = mutableListOf<EventCallback>()

    fun isEntered(): Boolean {
        return isEntered.get()
    }

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
        return childStage.get()?.last() ?: this
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

    fun addStage(name: String): LifecycleStage {
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
        val subscriptionsArray = subscriptions.toTypedArray()
        subscriptions.clear()
        subscriptionsArray.forEach {
            it.removeListener(this)
            it.unsubscribe()
        }
        childStage.get()?.exit()
        isEntered.set(false)
        exitCallback.execute()
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

    override fun doOnce(key: String, action: () -> Unit): LifecycleSubscription {
        if (isTerminated.get()) {
            return LifecycleSubscription()
        }
        if (isEntered.get()) {
            action()
            return LifecycleSubscription()
        } else {
            whenEnteredActions[key] = action
            return LifecycleSubscription {
                whenEnteredActions.remove(key)
            }
        }
    }

    override fun doOnEnter(callback: LifecycleStage.OnEnterHandler.() -> Unit): LifecycleSubscription {
        if (isEntered.get() || isTerminated.get()) {
            return LifecycleSubscription()
        }
        val eventCallback = subscribeEnter(callback)
        enterCallback.add(eventCallback)
        cancelOnExitFromActiveStage(eventCallback)
        return LifecycleSubscription {
            enterCallback.cancel(eventCallback)
        }
    }

    private fun subscribeExit(callback: LifecycleStage.OnExitHandler.() -> Unit): EventCallback {
        return EventCallback {
            exitHandler.get()?.callback()
        }
    }

    override fun doOnExit(callback: LifecycleStage.OnExitHandler.() -> Unit): LifecycleSubscription {
        if (isTerminated.get()) {
            return LifecycleSubscription()
        }
        val eventCallback = subscribeExit(callback)
        exitCallback.add(eventCallback)
        cancelOnExitFromActiveStage(eventCallback)
        return LifecycleSubscription {
            exitCallback.cancel(eventCallback)
        }
    }

    private inner class OnEnterHandlerImpl : LifecycleStage.OnEnterHandler {

        override fun onExit(callback: LifecycleStage.OnExitHandler.() -> Unit): LifecycleSubscription {
            return doOnExit(callback)
        }

    }

    private inner class OnExitHandlerImpl : LifecycleStage.OnExitHandler {

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

    override fun getStageName(): String {
        return this.name
    }

}