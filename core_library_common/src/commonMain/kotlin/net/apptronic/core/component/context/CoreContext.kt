package net.apptronic.core.component.context

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.component.di.DependencyDispatcher
import net.apptronic.core.component.lifecycle.BASE_LIFECYCLE
import net.apptronic.core.component.lifecycle.Lifecycle

fun coreContext(coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main, builder: Context.() -> Unit = {}): Context {
    return CoreContext(BASE_LIFECYCLE.createLifecycle(), coroutineDispatcher).apply(builder)
}

private class CoreContext(
        override val lifecycle: Lifecycle = Lifecycle(),
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main
) : BaseContext() {

    override val defaultDispatcher: CoroutineDispatcher = coroutineDispatcher

    init {
        requireNeverFrozen()
    }

    override val dependencyDispatcher = DependencyDispatcher(this, null)

    override val parent: Context? = null

}