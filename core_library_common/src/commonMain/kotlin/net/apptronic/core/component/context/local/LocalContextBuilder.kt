package net.apptronic.core.component.context.local

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.lifecycle.BASE_LIFECYCLE

fun Context.createLocalContext(
        dispatcher: CoroutineDispatcher = defaultDispatcher
): LocalContext {
    return LocalContext(parent = this, defaultDispatcher = dispatcher, lifecycleDefinition = BASE_LIFECYCLE)
}