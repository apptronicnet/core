package net.apptronic.core.context.lifecycle

import net.apptronic.core.context.plugin.Extensions
import net.apptronic.core.entity.EntitySubscription
import net.apptronic.core.entity.subscriptions.EntitySubscriptionListener

internal class LifecycleStageImpl(val parent: LifecycleStageParent,
                                  private val definition: LifecycleStageDefinition) :
        LifecycleStage, LifecycleStageParent, EntitySubscriptionListener {

    private var childStage: LifecycleStageImpl? = null

    private var isEntered = false
    private var isTerminated = false

    private val enterCallback = CompositeCallback()
    private val exitCallback = CompositeCallback()
    private val whenEnteredActions = HashMap<String, (() -> Unit)>()

    private var enterHandler: OnEnterHandlerImpl? = null
    private var exitHandler: OnExitHandlerImpl? = null
    private val subscriptions = mutableListOf<EntitySubscription>()

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
        childStage?.terminate()
        childStage = null
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
        return childStage?.last() ?: this
    }

    fun lastEntered(): LifecycleStageImpl? {
        return if (isEntered) {
            childStage?.lastEntered() ?: this
        } else null
    }

    fun findStage(stageDefinition: LifecycleStageDefinition): LifecycleStageImpl? {
        return if (stageDefinition === this.definition) {
            this
        } else {
            childStage?.findStage(stageDefinition)
        }
    }

    fun addStage(stageDefinition: LifecycleStageDefinition): LifecycleStage {
        childStage?.terminate()
        return LifecycleStageImpl(this@LifecycleStageImpl, stageDefinition).also {
            childStage = it
        }
    }

    internal fun enter() {
        if (isTerminated || isEntered) {
            return
        }
        parent.onChildEnter()
        enterHandler = OnEnterHandlerImpl()
        exitHandler = OnExitHandlerImpl()
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
        childStage?.exit()
        isEntered = false
        exitCallback.execute()
        inStageCallbacks.forEach { it.cancel() }
        inStageCallbacks.clear()
    }

    private fun subscribeEnter(callback: LifecycleStage.OnEnterHandler.() -> Unit): EventCallback {
        return EventCallback {
            enterHandler?.callback()
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
            exitHandler?.callback()
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
        if (isEntered) {
            cancelOnExit(eventCallback)
        } else {
            parent.cancelOnExitFromActiveStage(eventCallback)
        }
    }

    override fun getStageName(): String {
        return this.definition.name
    }

    override fun getStageDefinition(): LifecycleStageDefinition {
        return this.definition
    }

    override val extensions: Extensions = Extensions()

}