package net.apptronic.core.context

import net.apptronic.core.context.coroutines.CoroutineDispatchers
import net.apptronic.core.context.coroutines.standardCoroutineDispatchers
import net.apptronic.core.context.di.dependencyDispatcher
import net.apptronic.core.context.lifecycle.BASE_LIFECYCLE
import net.apptronic.core.context.lifecycle.Lifecycle

fun coreContext(coroutineDispatchers: CoroutineDispatchers = standardCoroutineDispatchers(), builder: Context.() -> Unit = {}): Context {
    return CoreContext(BASE_LIFECYCLE.createLifecycle(), coroutineDispatchers).apply(builder)
}

private class CoreContext(
        override val lifecycle: Lifecycle = Lifecycle(),
        override val coroutineDispatchers: CoroutineDispatchers
) : BaseContext() {

    override val dependencyDispatcher = dependencyDispatcher()

    override val parent: Context? = null

    init {
        plugins.attach(this)
    }

}