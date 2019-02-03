package net.apptronic.common.core.component.base

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.di.DIContext
import net.apptronic.common.core.component.lifecycle.Lifecycle
import net.apptronic.common.core.component.threading.ContextWorkers
import net.apptronic.common.core.component.threading.DefaultContextWorkers

class CoreComponentContext : ComponentContext {

    private val lifecycle = DefaultApplicationLifecycle(this)
    private val workers = DefaultContextWorkers()
    private val diContext = DIContext(this, null)

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    override fun workers(): ContextWorkers {
        return workers
    }

    override fun objects(): DIContext {
        return diContext
    }

}