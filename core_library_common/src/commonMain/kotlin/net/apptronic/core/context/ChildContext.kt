package net.apptronic.core.context

import net.apptronic.core.context.coroutines.CoroutineDispatchers

fun Contextual.childContext(
    builder: Context.() -> Unit = {}
): Context {
    val subContext = ChildContext(parent = context)
    this.context.lifecycle.registerChildLifecycle(subContext.lifecycle)
    subContext.plugins.attach(subContext)
    subContext.builder()
    return subContext
}

private class ChildContext(override val parent: Context) : BaseContext() {

    override val coroutineDispatchers: CoroutineDispatchers = parent.coroutineDispatchers

}