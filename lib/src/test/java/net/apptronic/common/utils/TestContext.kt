package net.apptronic.common.utils

import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.threading.ContextWorkers

open class TestContext : ComponentContext {

    private val workers = TestWorkers()
    private val lifecycle = TestLifecycle(workers)
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