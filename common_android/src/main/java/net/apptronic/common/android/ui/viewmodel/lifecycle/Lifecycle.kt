package net.apptronic.common.android.ui.viewmodel.lifecycle

import net.apptronic.common.android.ui.threading.ThreadExecutor
import net.apptronic.common.android.ui.threading.ThreadExecutorProvider
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

    private val isStarted = AtomicBoolean(false)
    private val isTerminated = AtomicBoolean(false)
    private val stages = LinkedList<LifecycleStage>()

    private val rootStage: LifecycleStage = createStage(ROOT_STAGE)

    private fun verifyStarted() {
        if (!isStarted.get()) {
            throw IllegalStateException("Lifecycle is not started yet")
        }
    }

    protected fun createStage(name: String): LifecycleStage {
        val stage = LifecycleStage(this, name)
        stages.add(stage)
        return stage
    }

    fun getActiveStage(): LifecycleStage {
        verifyStarted()
        return stages.lastOrNull { it.isEntered() } ?: rootStage
    }

    fun getStage(name: String): LifecycleStage? {
        verifyStarted()
        return stages.firstOrNull {
            it.name == name
        }
    }

    fun start() {
        if (isStarted.get()) {
            throw IllegalStateException("Lifecycle is already started")
        }
        if (isTerminated()) {
            return
        }
        isStarted.set(true)
        rootStage.enter()
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
        isTerminated.set(true)
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