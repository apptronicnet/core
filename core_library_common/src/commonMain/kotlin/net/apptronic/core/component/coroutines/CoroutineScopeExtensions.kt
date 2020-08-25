package net.apptronic.core.component.coroutines

import kotlinx.coroutines.*
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.getGlobalContext
import net.apptronic.core.component.lifecycle.LifecycleStage
import net.apptronic.core.component.lifecycle.LifecycleSubscription
import net.apptronic.core.component.plugin.extensionDescriptor
import kotlin.coroutines.CoroutineContext

private val LifecycleStageScopeExtensionDescriptor = extensionDescriptor<CoroutineScope>()

class LifecycleStageExitException internal constructor(reason: String) : CancellationException(reason)

class IsCancellableJob(val isCancellable: Boolean, val wrappedJob: Job = SupervisorJob()) : Job by wrappedJob {

    override fun cancel() {
        if (isCancellable) {
            wrappedJob.cancel()
        }
    }

    override fun cancel(cause: Throwable?): Boolean {
        if (isCancellable) {
            wrappedJob.cancel(CancellationException(cause?.message))
            return true
        }
        return false
    }

    override fun cancel(cause: CancellationException?) {
        if (isCancellable) {
            wrappedJob.cancel(cause)
        }
    }

    fun forceCancel(cause: CancellationException) {
        wrappedJob.cancel(cause)
    }

    override val key: CoroutineContext.Key<*>
        get() {
            return Job
        }

    override fun <R> fold(initial: R, operation: (R, CoroutineContext.Element) -> R): R {
        return operation(initial, this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <E : CoroutineContext.Element> get(key: CoroutineContext.Key<E>): E? {
        if (key == Job) {
            return this as E
        }
        return super.get(key)
    }

}

private fun retrieveLifecycleBoundScope(owner: Contextual, lifecycleStage: LifecycleStage): CoroutineScope {
    val existing = lifecycleStage.extensions[LifecycleStageScopeExtensionDescriptor]
    return if (existing == null || !existing.isActive) {
        return createLifecycleBoundScope(owner, lifecycleStage, true)
    } else existing
}

private fun createLifecycleBoundScope(owner: Contextual, lifecycleStage: LifecycleStage, attachAsExtension: Boolean): CoroutineScope {
    val stageName = if (lifecycleStage.isEntered()) lifecycleStage.getStageName() else "[terminated]"
    val name = "${owner::class.simpleName}/$stageName"
    val isCancellableByUser = attachAsExtension.not()
    val job = IsCancellableJob(isCancellableByUser)
    val coroutineContext: CoroutineContext = CoroutineName(name) + owner.mainDispatcher + job
    val coroutineScope = CoroutineScope(coroutineContext)
    val lifecycleSubscription: LifecycleSubscription? = if (lifecycleStage.isEntered()) {
        if (attachAsExtension) {
            lifecycleStage.extensions[LifecycleStageScopeExtensionDescriptor] = coroutineScope
        }
        lifecycleStage.doOnExit {
            if (attachAsExtension) {
                lifecycleStage.extensions.remove(LifecycleStageScopeExtensionDescriptor)
            }
            job.forceCancel(LifecycleStageExitException("Stage existed ${lifecycleStage.getStageName()}"))
        }
    } else {
        job.forceCancel(LifecycleStageExitException("Context was terminated"))
        null
    }
    if (lifecycleSubscription != null) {
        job.invokeOnCompletion {
            lifecycleSubscription.unsubscribe()
        }
    }
    return coroutineScope
}

/**
 * Get instance of [CoroutineScope] object which is attached to active stage or current [Context].
 *
 * This [CoroutineScope] ignores cancellation commands and cancelled only when active [LifecycleStage] of current
 * [Context] is exited.
 */
val Contextual.lifecycleCoroutineScope: CoroutineScope
    get() {
        val lifecycle = context.lifecycle
        return retrieveLifecycleBoundScope(this, lifecycle.getActiveStage() ?: lifecycle.rootStage)
    }

/**
 * Get instance of [CoroutineScope] object which is attached to root stage or current [Context].
 *
 * This [CoroutineScope] ignores cancellation commands and cancelled only when root [LifecycleStage] of current
 * [Context] is exited.
 */
val Contextual.contextCoroutineScope: CoroutineScope
    get() {
        val lifecycle = context.lifecycle
        return retrieveLifecycleBoundScope(this, lifecycle.rootStage)
    }


/**
 * Get instance of global [CoroutineScope] object which is attached to root stage or core [Context].
 *
 * This [CoroutineScope] ignores cancellation commands and cancelled only when root [LifecycleStage] of core [Context]
 * is exited.
 */
val Contextual.globalCoroutineScope: CoroutineScope
    get() {
        return context.getGlobalContext().contextCoroutineScope
    }

/**
 * Create new instance of [CoroutineScope] object which is attached to active stage or current [Context].
 *
 * Can be cancelled.
 */
fun Context.createLifecycleCoroutineScope(): CoroutineScope {
    val lifecycle = context.lifecycle
    return createLifecycleBoundScope(this, lifecycle.getActiveStage() ?: lifecycle.rootStage, false)
}

/**
 * Create new instance of [CoroutineScope] object which is attached to root stage or current [Context].
 *
 * Can be cancelled.
 */
fun Context.createContextCoroutineScope(): CoroutineScope {
    val lifecycle = context.lifecycle
    return createLifecycleBoundScope(this, lifecycle.rootStage, false)
}
