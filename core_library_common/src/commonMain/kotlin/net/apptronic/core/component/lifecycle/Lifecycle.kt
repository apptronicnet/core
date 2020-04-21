package net.apptronic.core.component.lifecycle

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.concurrent.Volatile
import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription

/**
 * Def
 */
open class Lifecycle {

    init {
        requireNeverFrozen()
    }

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
        const val ROOT_STAGE = "_root"
    }

    private val isTerminated = Volatile(false)

    fun currentStageCoroutineScope(): CoroutineScope {
        return getActiveStage()?.coroutineScope() ?: rootStage.coroutineScope()
    }

    fun launchCoroutine(block: suspend CoroutineScope.() -> Unit) {
        rootStage.launchCoroutine(block)
    }

    fun launchStagedCoroutine(block: suspend CoroutineScope.() -> Unit) {
        getActiveStage()?.launchCoroutine(block)
    }

    private val rootStage: LifecycleStageImpl = LifecycleStageImpl(baseParent, ROOT_STAGE)

    fun getRootStage(): LifecycleStage {
        return rootStage
    }

    init {
        rootStage.apply {
            enter()
            doOnExit {
                isTerminated.set(true)
                terminateChildren()
            }
        }
    }

    fun getStage(name: String): LifecycleStage? {
        return rootStage.stageByName(name)
    }

    fun getActiveStage(): LifecycleStage? {
        return rootStage.lastEntered()
    }

    fun isStageEntered(name: String): Boolean {
        return rootStage.stageByName(name)?.isEntered() ?: false
    }

    fun addStage(name: String): LifecycleStage {
        return rootStage.last().addStage(name)
    }

    fun onExitFromActiveStage(action: () -> Unit) {
        getActiveStage()?.let {
            it.doOnExit {
                action()
            }
        } ?: run(action)
    }

    fun doOnTerminate(action: () -> Unit) {
        if (rootStage.isEntered()) {
            rootStage.doOnExit {
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
        return isTerminated.get()
    }

    internal fun enterStage(name: String) {
        rootStage.stageByName(name)?.enter()
    }

    internal fun exitStage(name: String) {
        rootStage.stageByName(name)?.exit()
    }

    fun terminate() {
        rootStage.terminate()
    }

    fun registerSubscription(subscription: EntitySubscription) {
        rootStage.lastEntered()?.let {
            it.registerSubscription(subscription)
        } ?: run {
            subscription.unsubscribe()
        }
    }

}

fun enterStage(context: Context?, name: String) {
    context?.getLifecycle()?.enterStage(name)
}

fun exitStage(context: Context?, name: String) {
    context?.getLifecycle()?.exitStage(name)
}
