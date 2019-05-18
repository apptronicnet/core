package net.apptronic.core.component.context

import net.apptronic.core.base.Logger
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.platform.Platform
import net.apptronic.core.component.platform.PlatformHandler
import net.apptronic.core.threading.Scheduler
import net.apptronic.core.threading.createCoreScheduler

open class CoreContext(
    platform: Platform,
    private val lifecycle: Lifecycle = Lifecycle()
) : Context {

    private val platformHandler = PlatformHandler(platform)
    private val logger = Logger().apply {
        setupLogging {
            platformHandler.logMessage(it)
        }
    }
    private val scheduler = createCoreScheduler(platformHandler)
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

}