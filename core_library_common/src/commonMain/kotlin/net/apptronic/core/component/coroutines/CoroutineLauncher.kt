package net.apptronic.core.component.coroutines

import kotlinx.coroutines.*
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.getGlobalContext
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage
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
    )

}

/**
 * Create [CoroutineLauncher] which [CoroutineScope] is bound to [LifecycleStage] which is active at
 * the creation of [CoroutineLauncher]. [CoroutineScope] wil be automatically cancelled when bound [LifecycleStage] will
 * exit throwing [CoroutineLauncherCancellationException]
 */
fun Context.coroutineLauncherScoped(): CoroutineLauncher {
    val boundStage = getLifecycle().getActiveStage()
    val name = "${this::class.simpleName}/{${boundStage?.getStageName() ?: "[terminated]"}"
    val coroutineContext: CoroutineContext = CoroutineName(name)
    boundStage?.doOnExit {
        coroutineContext.cancel(CoroutineLauncherCancellationException("Stage existed ${boundStage.getStageName()}"))
    } ?: run {
        coroutineContext.cancel(CoroutineLauncherCancellationException("Context was terminated"))
    }
    val coroutineScope = CoroutineScope(coroutineContext)
    return CoroutineLauncherImpl(this, coroutineScope)
}

/**
 * Create [CoroutineLauncher] which [CoroutineScope] is bound to while [Lifecycle] of [Context].
 * [CoroutineScope] wil be automatically cancelled when [Lifecycle] will be terminated
 * throwing [CoroutineLauncherCancellationException]
 */
fun Context.coroutineLauncherContextual(): CoroutineLauncher {
    val name = "${this::class.simpleName}"
    val coroutineContext = if (!getLifecycle().isTerminated()) {
        CoroutineName(name).also {
            getLifecycle().doOnTerminate {
                it.cancel(CoroutineLauncherCancellationException("Context terminated"))
            }
        }
    } else {
        CoroutineName("$name [terminated]").also {
            it.cancel(CoroutineLauncherCancellationException("Context was terminated"))
        }
    }
    val coroutineScope = CoroutineScope(coroutineContext)
    return CoroutineLauncherImpl(this, coroutineScope)
}

/**
 * Same as [coroutineLauncherContextual] except it will be created in top level [Context] of current context tree
 */
fun Context.coroutineLauncherGlobal(): CoroutineLauncher {
    return getGlobalContext().coroutineLauncherContextual()
}

internal class CoroutineLauncherImpl(override val context: Context, override val coroutineScope: CoroutineScope) : CoroutineLauncher {

    override fun launch(coroutineContext: CoroutineContext, start: CoroutineStart, block: suspend CoroutineScope.() -> Unit) {
        coroutineScope.launch(coroutineContext, start) {
            try {
                block()
            } catch (e: CoroutineLauncherCancellationException) {
                // ignore
            }
        }
    }

}