package net.apptronic.core.component.lifecycle

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.entity.EntitySubscription

/**
 * Defines lifecycle.
 */
class Lifecycle internal constructor() {

    private inner class BaseParent : LifecycleStageParent {

        override fun onChildEnter() {
            // ignore
        }

        override fun cancelOnExitFromActiveStage(eventCallback: EventCallback) {
            // should not go here
            // but may go in case if all is terminated
            eventCallback.cancel()
        }
    }

    private val baseParent = BaseParent()

    companion object {
        val ROOT_STAGE = lifecycleStage("default_root_stage")
    }

    private var isTerminated = false

    private val rootStageImpl: LifecycleStageImpl = LifecycleStageImpl(baseParent, ROOT_STAGE)

    val rootStage: LifecycleStage = rootStageImpl

    init {
        rootStageImpl.apply {
            enter()
            doOnExit {
                isTerminated = true
                terminateChildren()
            }
        }
    }

    operator fun get(definition: LifecycleStageDefinition): LifecycleStage? {
        return rootStageImpl.findStage(definition)
    }

    fun getActiveStage(): LifecycleStage? {
        return rootStageImpl.lastEntered()
    }

    fun isStageEntered(definition: LifecycleStageDefinition): Boolean {
        return rootStageImpl.findStage(definition)?.isEntered() ?: false
    }

    internal fun addStage(definition: LifecycleStageDefinition): LifecycleStage {
        return rootStageImpl.last().addStage(definition)
    }

    fun onExitFromActiveStage(action: () -> Unit) {
        getActiveStage()?.let {
            it.doOnExit {
                action()
            }
        } ?: run(action)
    }

    fun doOnTerminate(action: () -> Unit) {
        if (rootStageImpl.isEntered()) {
            rootStageImpl.doOnExit {
                action.invoke()
            }
        } else {
            action.invoke()
        }
    }

    private val children = mutableListOf<Lifecycle>()

    fun registerChildLifecycle(child: Lifecycle) {
        if (isTerminated()) {
            child.terminate()
        } else {
            children.add(child)
            child.doOnTerminate {
                children.remove(child)
            }
        }
    }

    private fun terminateChildren() {
        val copy = children.toTypedArray()
        children.clear()
        copy.forEach {
            it.terminate()
        }
    }

    fun isTerminated(): Boolean {
        return isTerminated
    }

    internal fun enterStage(definition: LifecycleStageDefinition) {
        rootStageImpl.findStage(definition)?.enter()
    }

    internal fun exitStage(definition: LifecycleStageDefinition) {
        rootStageImpl.findStage(definition)?.exit()
    }

    fun terminate() {
        rootStageImpl.terminate()
    }

    fun registerSubscription(subscription: EntitySubscription) {
        rootStageImpl.lastEntered()?.let {
            it.registerSubscription(subscription)
        } ?: run {
            subscription.unsubscribe()
        }
    }

}

fun Contextual.enterStage(definition: LifecycleStageDefinition) {
    context.lifecycle.enterStage(definition)
}

fun Contextual.exitStage(definition: LifecycleStageDefinition) {
    context.lifecycle.exitStage(definition)
}

fun enterStage(context: Context?, definition: LifecycleStageDefinition) {
    context?.lifecycle?.enterStage(definition)
}

fun exitStage(context: Context?, definition: LifecycleStageDefinition) {
    context?.lifecycle?.exitStage(definition)
}
