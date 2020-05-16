package net.apptronic.core.component.context

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.component.di.DependencyDispatcher
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleDefinition

open class SubContext : BaseContext {

    final override val parent: Context
    final override val lifecycle: Lifecycle
    final override val defaultDispatcher: CoroutineDispatcher
    final override val dependencyDispatcher: DependencyDispatcher

    constructor(parent: Context, lifecycleDefinition: LifecycleDefinition) : super() {
        this.parent = parent
        this.lifecycle = lifecycleDefinition.createLifecycle()
        this.defaultDispatcher = parent.defaultDispatcher
        this.dependencyDispatcher = DependencyDispatcher(this, parent.dependencyDispatcher)
        init()
    }

    constructor(parent: Context, lifecycle: Lifecycle) : super() {
        this.parent = parent
        this.lifecycle = lifecycle
        this.defaultDispatcher = parent.defaultDispatcher
        this.dependencyDispatcher = DependencyDispatcher(this, parent.dependencyDispatcher)
        init()
    }

    init {
        requireNeverFrozen()
    }

    private fun init() {
        parent.lifecycle.registerChildLifecycle(lifecycle)
        parent.plugins.let { parentPlugins ->
            parentPlugins.descriptors.forEach { descriptor ->
                parentPlugins[descriptor]?.let { plugin ->
                    pluginsImpl.add(descriptor, plugin)
                    plugin.onContext(this)
                }
            }
        }
    }

}