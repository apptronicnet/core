package net.apptronic.core.context

import net.apptronic.core.context.coroutines.CoroutineDispatchers
import net.apptronic.core.context.coroutines.standardCoroutineDispatchers

fun coreContext(
    coroutineDispatchers: CoroutineDispatchers = standardCoroutineDispatchers(),
    builder: Context.() -> Unit = {}
): Context {
    val context = CoreContext(coroutineDispatchers = coroutineDispatchers)
    context.plugins.attach(context)
    context.apply(builder)
    return context
}

private class CoreContext(
    override val coroutineDispatchers: CoroutineDispatchers
) : BaseContext() {

    override val parent: Context? = null

    init {
        plugins.attach(this)
    }

}