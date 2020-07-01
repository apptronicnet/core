package net.apptronic.core.component.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage
import kotlin.coroutines.CoroutineContext

interface CoroutineLaunchers {

    /**
     * Same as [local] except it will be created in top level [Context] of current context tree
     */
    val global: CoroutineLauncher

    /**
     * Create [CoroutineLauncher] which [CoroutineScope] is bound to while [Lifecycle] of [Context].
     * [CoroutineScope] wil be automatically cancelled when [Lifecycle] will be terminated
     * throwing [CoroutineLauncherCancellationException]
     */
    val local: CoroutineLauncher

    /**
     * Create [CoroutineLauncher] which [CoroutineScope] is bound to [LifecycleStage] which is active at
     * the creation of [CoroutineLauncher]. [CoroutineScope] wil be automatically cancelled when bound [LifecycleStage] will
     * exit throwing [CoroutineLauncherCancellationException]
     */
    val scoped: CoroutineLauncher

}

fun Context.coroutineLaunchers(): CoroutineLaunchers {
    return CoroutineLaunchersImpl(this)
}

fun Component.coroutineLaunchers(): CoroutineLaunchers {
    return CoroutineLaunchersImpl(context)
}

fun Component.coroutineDispatcher(descriptor: CoroutineDispatcherDescriptor): CoroutineDispatcher {
    return context.coroutineDispatchers[descriptor]
}

/**
 * [CoroutineDispatcher] which matches to [Dispatchers.Main] (if not overridden by [Context])
 */
val Component.mainDispatcher: CoroutineContext
    get() {
        return coroutineDispatcher(MainDispatcherDescriptor)
    }

/**
 * [CoroutineDispatcher] which matches to [Dispatchers.Default] (if not overridden by [Context])
 */
val Component.defaultDispatcher: CoroutineContext
    get() {
        return coroutineDispatcher(BackgroundDispatcherDescriptor)
    }

/**
 * [CoroutineDispatcher] which matches to [Dispatchers.Unconfined] (if not overridden by [Context])
 */
val Component.unconfinedDispatcher: CoroutineContext
    get() {
        return coroutineDispatcher(UnconfinedDispatcherDescriptor)
    }

/**
 * Get [CoroutineContext] which uses to [Dispatchers.Default] and executes all coroutines with specified
 * [priority] (if not overridden by [Context])
 */
fun Component.priorityDispatcher(priority: Int = PRIORITY_MEDIUM): CoroutineContext {
    return context.coroutineDispatchers[BackgroundPriorityDispatcherDescriptor].withPriority(priority)
}

private class CoroutineLaunchersImpl(private val context: Context) : CoroutineLaunchers {

    override val global: CoroutineLauncher
        get() = context.coroutineLauncherGlobal()

    override val local: CoroutineLauncher
        get() = context.coroutineLauncherLocal()

    override val scoped: CoroutineLauncher
        get() = context.coroutineLauncherScoped()

}