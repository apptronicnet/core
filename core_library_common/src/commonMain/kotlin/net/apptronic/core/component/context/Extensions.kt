package net.apptronic.core.component.context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.component.Component
import net.apptronic.core.component.coroutines.contextCoroutineScope
import net.apptronic.core.component.lifecycle.Lifecycle

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
fun Contextual.doAsync(coroutineScope: CoroutineScope = contextCoroutineScope, action: () -> Unit) {
    coroutineScope.launch {
        action()
    }
}