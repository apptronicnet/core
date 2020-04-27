package net.apptronic.core.component.extensions

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.SubContext
import net.apptronic.core.component.lifecycle.Lifecycle

fun buildContext(
        parent: Context,
        lifecycle: Lifecycle = Lifecycle(),
        defaultDispatcher: CoroutineDispatcher = parent.defaultDispatcher,
        block: Context.() -> Unit
): Context {
    val context = SubContext(parent, lifecycle, defaultDispatcher)
    context.block()
    return context
}