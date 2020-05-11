package net.apptronic.core.component.context

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.component.di.DependencyDispatcher
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleDefinition

open class SubContext(
        private val parent: Context,
        lifecycleDefinition: LifecycleDefinition
) : Context {

    final override val lifecycle: Lifecycle = lifecycleDefinition.createLifecycle()

    init {
        requireNeverFrozen()
        parent.lifecycle.registerChildLifecycle(lifecycle)
    }

    override val defaultDispatcher: CoroutineDispatcher = parent.defaultDispatcher

    private val dependencyProvider = DependencyDispatcher(this, parent.dependencyDispatcher())

    override fun getParent(): Context? {
        return parent
    }

    final override fun dependencyDispatcher(): DependencyDispatcher {
        return dependencyProvider
    }

}