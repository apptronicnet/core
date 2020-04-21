package net.apptronic.core.threading

import net.apptronic.core.base.concurrent.AtomicEntity
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.lifecycle.LifecycleSubscription

/**
 * This variant of provider wraps all actions and listens for lifecycle of context from which they are called.
 * After exit from stage actions are cancelled so it can be useful to wrap queue actioned workers to prevent executing
 * actions for which context was destroyed
 */
@Deprecated("Should use coroutines")
class ContextBoundWorkerProvider(
        private val targetWorker: Worker
) : WorkerProvider {

    override fun provideWorker(context: Context): Worker {
        val defaultWorker = context.getScheduler().getWorker(WorkerDefinition.DEFAULT)
        return WorkerWrapper(context, defaultWorker, targetWorker)
    }

}

@Deprecated("Should use coroutines")
private class WorkerWrapper(
        val context: Context,
        val defaultWorker: Worker,
        val targetWorker: Worker
) : Worker {

    override fun execute(action: Action) {
        targetWorker.execute(ActionWrapper(defaultWorker, context, action))
    }

}

@Deprecated("Should use coroutines")
private class ActionWrapper(
        val defaultWorker: Worker,
        val context: Context,
        val target: Action
) : Action {

    private val isActual = AtomicEntity<Boolean>(true)
    private val lifecycleSubscription: LifecycleSubscription?

    init {
        val activeStage = context.getLifecycle().getActiveStage()
        if (activeStage != null) {
            lifecycleSubscription = activeStage.doOnExit {
                isActual.set(false)
            }
        } else {
            isActual.set(false)
            lifecycleSubscription = null
        }
    }

    private val releaseAction = lambdaAction {
        lifecycleSubscription?.unsubscribe()
    }

    override fun execute() {
        defaultWorker.execute(releaseAction)
        if (isActual.get()) {
            target.execute()
        }
    }

}