package net.apptronic.core.component.context

import net.apptronic.core.base.Logger
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.threading.Scheduler
import net.apptronic.core.threading.createCoreScheduler

open class CoreContext(
    private val lifecycle: Lifecycle = Lifecycle()
) : Context {

    private val logger = Logger()
    private val scheduler = createCoreScheduler()
    private val dependencyProvider = DependencyProvider(this, null)

    override fun getLogger(): Logger {
        return logger
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    override fun getScheduler(): Scheduler {
        return scheduler
    }

    override fun getProvider(): DependencyProvider {
        return dependencyProvider
    }

    fun setLogging(logMethod: (String) -> Unit) {
        logger.setupLogging(logMethod)
    }

}