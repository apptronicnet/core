package net.apptronic.core.component.context

import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.DefaultApplicationLifecycle
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.threading.ContextWorkers
import net.apptronic.core.component.threading.DefaultContextWorkers

class CoreComponentContext : ComponentContext {

    private val lifecycle =
        DefaultApplicationLifecycle(this)
    private val workers = DefaultContextWorkers()
    private val diContext = DependencyProvider(this, null)

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    override fun workers(): ContextWorkers {
        return workers
    }

    override fun objects(): DependencyProvider {
        return diContext
    }

}