package net.apptronic.core.component.context

import net.apptronic.core.component.coroutines.CoroutineDispatchers
import net.apptronic.core.component.di.DependencyDispatcher
import net.apptronic.core.component.di.dependencyDispatcher
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleDefinition

open class SubContext : BaseContext {

    final override val parent: Context
    final override val lifecycle: Lifecycle
    final override val dependencyDispatcher: DependencyDispatcher
    final override val coroutineDispatchers: CoroutineDispatchers

    constructor(parent: Context, lifecycleDefinition: LifecycleDefinition) : super() {
        this.parent = parent
        this.lifecycle = lifecycleDefinition.createLifecycle()
        this.coroutineDispatchers = parent.coroutineDispatchers
        this.dependencyDispatcher = dependencyDispatcher()
        init()
    }

    constructor(parent: Context, lifecycle: Lifecycle) : super() {
        this.parent = parent
        this.lifecycle = lifecycle
        this.coroutineDispatchers = parent.coroutineDispatchers
        this.dependencyDispatcher = dependencyDispatcher()
        init()
    }

    private fun init() {
        parent.lifecycle.registerChildLifecycle(lifecycle)
        plugins.attach(this)
    }

}