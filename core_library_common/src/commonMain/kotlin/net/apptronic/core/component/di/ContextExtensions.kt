package net.apptronic.core.component.di

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.SubContext

/**
 * Execute some [action] in isolated context which will be immediately recycled after action execution
 */
fun <T> Context.isolatedExecute(action: Context.() -> T): T {
    val context = SubContext(this)
    return try {
        action.invoke(context)
    } finally {
        context.getLifecycle().terminate()
    }
}