package net.apptronic.core.component.context

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.component.di.DependencyDispatcher
import net.apptronic.core.component.lifecycle.Lifecycle

open class SubContext(
        private val parent: Context,
        private val lifecycle: Lifecycle = Lifecycle(),
        override val defaultDispatcher: CoroutineDispatcher = parent.defaultDispatcher
) : Context {

    init {
        requireNeverFrozen()
        parent.getLifecycle().registerChildLifecycle(lifecycle)
    }

    private val dependencyProvider = DependencyDispatcher(this, parent.dependencyDispatcher())

    override fun getParent(): Context? {
        return parent
    }

    final override fun dependencyDispatcher(): DependencyDispatcher {
        return dependencyProvider
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

}