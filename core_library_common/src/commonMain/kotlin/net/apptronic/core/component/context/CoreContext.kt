package net.apptronic.core.component.context

import net.apptronic.core.component.coroutines.CoroutineDispatchers
import net.apptronic.core.component.coroutines.standardCoroutineDispatchers
import net.apptronic.core.component.di.dependencyDispatcher
import net.apptronic.core.component.lifecycle.BASE_LIFECYCLE
import net.apptronic.core.component.lifecycle.Lifecycle

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