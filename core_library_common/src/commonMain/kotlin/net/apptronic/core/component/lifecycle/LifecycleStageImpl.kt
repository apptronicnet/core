package net.apptronic.core.component.lifecycle

import net.apptronic.core.base.concurrent.AtomicEntity
import net.apptronic.core.base.concurrent.Volatile
import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscriptions.EntitySubscriptionListener

internal class LifecycleStageImpl(val parent: LifecycleStageParent,
                                  private val definition: LifecycleStageDefinition) :
        LifecycleStage, LifecycleStageParent, EntitySubscriptionListener {

    init {
        requireNeverFrozen()
    }

    private val childStage = AtomicEntity<LifecycleStageImpl?>(null)

    private var isEntered = false
    private var isTerminated = false

    private val enterCallback = CompositeCallback()
    private val exitCallback = CompositeCallback()
    private val whenEnteredActions = requireNeverFrozen(HashMap<String, (() -> Unit)>())

    private val enterHandler = Volatile<OnEnterHandlerImpl?>(null)
    private val exitHandler = Volatile<OnExitHandlerImpl?>(null)
    private val subscriptions = requireNeverFrozen(mutableListOf<EntitySubscription>())

    /**
     * This callbacks are internally created to be executed on exit stage command
     */
    private val inStageCallbacks = mutableListOf<EventCallback>()

    override fun toString(): String {
        return super.toString() + " ${definition.name} isEntered=${isEntered}"
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

    override fun isEntered(): Boolean {
        return isEntered
    }

    fun terminate() {
        childStage.perform {
            get()?.terminate()
            set(null)
        }
        if (isEntered) {
            exit()
        }
        isTerminated = true
    }

    override fun onChildEnter() {
        if (!isEntered) {
            enter()
        }
    }

    fun last(): LifecycleStageImpl {
        return childStage.get()?.last() ?: this
    }

    fun lastEntered(): LifecycleStageImpl? {
        return if (isEntered) {
            childStage.get()?.lastEntered() ?: this
        } else null
    }

    fun findStage(stageDefinition: LifecycleStageDefinition): LifecycleStageImpl? {
        return if (stageDefinition === this.definition) {
            this
        } else {
            childStage.get()?.findStage(stageDefinition)
        }
    }

    fun addStage(stageDefinition: LifecycleStageDefinition): LifecycleStage {
        return childStage.perform {
            get()?.terminate()
            LifecycleStageImpl(this@LifecycleStageImpl, stageDefinition).also {
                set(it)
            }
        }
    }

    internal fun enter() {
        if (isTerminated || isEntered) {
            return
        }
        parent.onChildEnter()
        enterHandler.set(OnEnterHandlerImpl())
        exitHandler.set(OnExitHandlerImpl())
        isEntered = true
        enterCallback.execute()
        whenEnteredActions.values.forEach { it.invoke() }
        whenEnteredActions.clear()
    }

    internal fun exit() {
        if (isTerminated || !isEntered) {
            return
        }
        val subscriptionsArray = subscriptions.toTypedArray()
        subscriptions.clear()
        subscriptionsArray.forEach {
            it.removeListener(this)
            it.unsubscribe()
        }
        childStage.get()?.exit()
        isEntered = false
        exitCallback.execute()
        inStageCallbacks.forEach { it.cancel() }
        inStageCallbacks.clear()
    }

    private fun subscribeEnter(callback: LifecycleStage.OnEnterHandler.() -> Unit): EventCallback {
        return EventCallback {
            enterHandler.get()?.callback()
        }.apply {
            if (isEntered) {
                execute()
            }
        }
    }

    override fun doOnce(action: () -> Unit) {
        doOnce("", action)
    }

    override fun doOnce(key: String, action: () -> Unit): LifecycleSubscription {
        if (isTerminated) {
            return LifecycleSubscription()
        }
        if (isEntered) {
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
        if (isEntered || isTerminated) {
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
        if (isTerminated) {
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
        init {
            requireNeverFrozen()
        }

        override fun onExit(callback: LifecycleStage.OnExitHandler.() -> Unit): LifecycleSubscription {
            return doOnExit(callback)
        }

    }

    private inner class OnExitHandlerImpl : LifecycleStage.OnExitHandler {

        init {
            requireNeverFrozen()
        }
    }

    private fun cancelOnExit(callback: EventCallback) {
        inStageCallbacks.add(callback)
    }

    override fun cancelOnExitFromActiveStage(eventCallback: EventCallback) {
        if (isEntered) {
            cancelOnExit(eventCallback)
        } else {
            parent.cancelOnExitFromActiveStage(eventCallback)
        }
    }

    override fun getStageName(): String {
        return this.definition.name
    }

}