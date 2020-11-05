package net.apptronic.core.context

import net.apptronic.core.context.lifecycle.BASE_LIFECYCLE

fun Context.getGlobalContext(): Context {
    var global = this
    var complete = false
    do {
        global.parent?.let {
            global = it
        } ?: run {
            complete = true
        }
    } while (!complete)
    return global
}

/**
 * Execute some [action] in isolated context which will be immediately recycled after action execution
 */
fun <T> Context.isolatedExecute(action: Context.() -> T): T {
    val context = SubContext(this, BASE_LIFECYCLE)
    return try {
        action.invoke(context)
    } finally {
        context.lifecycle.terminate()
    }
}