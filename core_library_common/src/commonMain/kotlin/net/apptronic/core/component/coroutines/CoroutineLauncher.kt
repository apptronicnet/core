package net.apptronic.core.component.coroutines

import kotlinx.coroutines.*
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.getGlobalContext
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage
import net.apptronic.core.component.lifecycle.LifecycleSubscription
import kotlin.coroutines.CoroutineContext

class CoroutineLauncherCancellationException internal constructor(val reason: String) : CancellationException(reason)

/**
 * Class which allows to safely launch coroutines in context of selected scope. Scope will be automatically cancelled
 * according to [Lifecycle] of [Context]
 */
interface CoroutineLauncher {

    val context: Context

    val coroutineScope: CoroutineScope

    /**
     * Launch coroutine in context of [CoroutineScope]. Coroutine will automatically handle
     * [CoroutineLauncherCancellationException] and do no throw it upwards.
     */
    fun launch(
            coroutineContext: CoroutineContext = context.defaultDispatcher,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            block: suspend CoroutineScope.() -> Unit
    ): Job

}

private fun Context.coroutineLauncherStaged(lifecycleStage: LifecycleStage?): CoroutineLauncher {
    val name = "${this::class.simpleName}/{${lifecycleStage?.getStageName() ?: "[terminated]"}"
    val coroutineContext: CoroutineContext = CoroutineName(name) + Job()
    val coroutineScope = CoroutineScope(coroutineContext)
    val lifecycleSubscription: LifecycleSubscription? = lifecycleStage?.doOnExit {
        coroutineContext.cancel(CoroutineLauncherCancellationException("Stage existed ${lifecycleStage.getStageName()}"))
    } ?: run {
        coroutineContext.cancel(CoroutineLauncherCancellationException("Context was terminated"))
        null
    }
    return CoroutineLauncherImpl(this, coroutineScope, lifecycleSubscription)
}

internal fun Context.coroutineLauncherScoped(): CoroutineLauncher {
    return coroutineLauncherStaged(lifecycle.getActiveStage())
}

internal fun Context.coroutineLauncherLocal(): CoroutineLauncher {
    val rootStageOrNull = if (lifecycle.isTerminated()) null else lifecycle.rootStage
    return coroutineLauncherStaged(rootStageOrNull)
}

internal fun Context.coroutineLauncherGlobal(): CoroutineLauncher {
    return getGlobalContext().coroutineLauncherLocal()
}

private class CoroutineLauncherImpl(
        override val context: Context,
        override val coroutineScope: CoroutineScope,
        private val lifecycleSubscription: LifecycleSubscription?
) : CoroutineLauncher {

    init {
        if (lifecycleSubscription != null) {
            val job: Job? = coroutineScope.coroutineContext[Job]
            if (job != null) {
                job.invokeOnCompletion {
                    lifecycleSubscription.unsubscribe()
                }
            } else {
                lifecycleSubscription.unsubscribe()
            }
        }
    }

    override fun launch(coroutineContext: CoroutineContext, start: CoroutineStart, block: suspend CoroutineScope.() -> Unit): Job {
        return coroutineScope.launch(coroutineContext, start) {
            try {
                block()
            } catch (e: CoroutineLauncherCancellationException) {
                // ignore
            }
        }
    }

}