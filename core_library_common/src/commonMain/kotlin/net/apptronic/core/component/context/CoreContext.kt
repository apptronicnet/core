package net.apptronic.core.component.context

import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.component.coroutines.CoroutineDispatchers
import net.apptronic.core.component.coroutines.standardCoroutineDispatchers
import net.apptronic.core.component.di.DependencyDispatcher
import net.apptronic.core.component.lifecycle.BASE_LIFECYCLE
import net.apptronic.core.component.lifecycle.Lifecycle

fun coreContext(coroutineDispatchers: CoroutineDispatchers = standardCoroutineDispatchers(), builder: Context.() -> Unit = {}): Context {
    return CoreContext(BASE_LIFECYCLE.createLifecycle(), coroutineDispatchers).apply(builder)
}

private class CoreContext(
        override val lifecycle: Lifecycle = Lifecycle(),
        override val coroutineDispatchers: CoroutineDispatchers
) : BaseContext() {

    init {
        requireNeverFrozen()
    }

    override val dependencyDispatcher = DependencyDispatcher(this, null)

    override val parent: Context? = null

}