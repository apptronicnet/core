package net.apptronic.common.core.mvvm.viewmodel.lifecycle

import net.apptronic.common.core.mvvm.threading.ThreadExecutor
import net.apptronic.common.core.mvvm.threading.ThreadExecutorProvider
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Def
 */
open class Lifecycle(provider: ThreadExecutorProvider) : ThreadExecutorProvider {

    private val threadExecutor: ThreadExecutor = provider.provideThreadExecutor()

    override fun provideThreadExecutor(): ThreadExecutor {
        return threadExecutor
    }

    companion object {
        const val ROOT_STAGE = "_root"
    }

    private val isTerminated = AtomicBoolean(false)
    private val stages = LinkedList<LifecycleStage>()

    private val rootStage: LifecycleStage = createStage(ROOT_STAGE).apply {
        enter()
        doOnExit {
            isTerminated.set(true)
        }
    }

    protected fun createStage(name: String): LifecycleStage {
        val stage = LifecycleStage(this, name)
        stages.add(stage)
        return stage
    }

    fun getActiveStage(): LifecycleStage {
        return stages.lastOrNull { it.isEntered() } ?: rootStage
    }

    fun getStage(name: String): LifecycleStage? {
        return stages.firstOrNull {
            it.name == name
        }
    }

    /**
     * Called when model is terminated. Lifecycle should be immediately fully exited,
     * all subscriptions are dropped
     */
    fun finish() {
        if (isTerminated()) {
            return
        }
        stages.reversed().forEach {
            if (it.isEntered()) {
                it.exit()
            }
        }
    }

    fun isTerminated(): Boolean {
        return isTerminated.get()
    }

}

fun enterStage(lifecycleHolder: LifecycleHolder?, name: String) {
    lifecycleHolder?.let {
        it.getLifecycle().getStage(name)?.enter()
    }
}

fun exitStage(lifecycleHolder: LifecycleHolder?, name: String) {
    lifecycleHolder?.let {
        it.getLifecycle().getStage(name)?.exit()
    }
}