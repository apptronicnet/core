package net.apptronic.core.component.context.local

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.component.context.Context

fun Context.createLocalContext(
        dispatcher: CoroutineDispatcher = defaultDispatcher
): LocalContext {
    val stageName = getLifecycle().let {
        it.getActiveStage() ?: it.getRootStage()
    }.getStageName()
    val localLifecycle = LocalLifecycle(stageName)
    return LocalContext(parent = this, defaultDispatcher = dispatcher, lifecycle = localLifecycle)
}