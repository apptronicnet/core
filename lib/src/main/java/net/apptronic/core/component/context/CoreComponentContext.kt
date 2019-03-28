package net.apptronic.core.component.context

import net.apptronic.core.base.ComponentLogger
import net.apptronic.core.base.ComponentLoggerDescriptor
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.DefaultApplicationLifecycle
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.threading.ContextWorkers
import net.apptronic.core.component.threading.DefaultContextWorkers

open class CoreComponentContext : ComponentContext {

    private val workers = DefaultContextWorkers()
    private val lifecycle = DefaultApplicationLifecycle(this)
    private val diContext = DependencyProvider(this, null)

    private val componentLogger = ComponentLogger()

    init {
        diContext.addInstance(ComponentLoggerDescriptor, componentLogger)
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    override fun workers(): ContextWorkers {
        return workers
    }

    override fun objects(): DependencyProvider {
        return diContext
    }

    fun setLogging(logMethod: (String) -> Unit) {
        componentLogger.logMethod = logMethod
    }

}