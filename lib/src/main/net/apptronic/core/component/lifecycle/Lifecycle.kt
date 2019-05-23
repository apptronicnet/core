package net.apptronic.core.component.lifecycle

import net.apptronic.core.component.context.Context

/**
 * Def
 */
open class Lifecycle {

    private val baseParent = object : LifecycleStageParent {

        override fun onChildEnter() {
            // ignore
        }

        override fun cancelOnExitFromActiveStage(eventCallback: EventCallback) {
            // should not go here
            // but may go in case if all is terminated
            eventCallback.cancel()
        }
    }

    companion object {
        const val ROOT_STAGE = "_root"
    }

    private val isTerminated = AtomicBoolean(false)

    private val rootStage: LifecycleStageImpl = LifecycleStageImpl(baseParent, ROOT_STAGE)

    init {
        rootStage.apply {
            enter()
            doOnExit {
                isTerminated.set(true)
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

}

fun enterStage(context: Context?, name: String) {
    context?.getLifecycle()?.enterStage(name)
}

fun exitStage(context: Context?, name: String) {
    context?.getLifecycle()?.exitStage(name)
}