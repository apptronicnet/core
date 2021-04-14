package net.apptronic.core.context

fun Context.getGlobalContext(): Context {
    var iteration = this
    do {
        iteration.parent?.let {
            iteration = it
        } ?: run {
            return iteration
        }
    } while (true)
}

/**
 * Execute some [action] in isolated context which will be immediately recycled after action execution
 */
fun <T> Context.isolatedExecute(action: Context.() -> T): T {
    val context = childContext()
    return try {
        action.invoke(context)
    } finally {
        context.lifecycle.terminate()
    }
}