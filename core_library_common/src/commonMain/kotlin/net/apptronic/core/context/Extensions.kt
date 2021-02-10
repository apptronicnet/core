package net.apptronic.core.context

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.context.lifecycle.Lifecycle

/**
 * Terminate [Context] lifecycle. This action cause termination of all [Component]s in this [Context]
 */
fun Contextual.terminate() {
    context.lifecycle.terminate()
}

/**
 * Execute asynchronous action. This may be useful if action is navigation action to prevent immediate [Context]
 * termination of [Lifecycle] changes.
 */
fun Contextual.doAsync(
    coroutineScope: CoroutineScope = contextCoroutineScope,
    action: suspend CoroutineScope.() -> Unit
) {
    coroutineScope.launch {
        try {
            action()
        } catch (e: CancellationException) {
            // ignore
        }
    }
}