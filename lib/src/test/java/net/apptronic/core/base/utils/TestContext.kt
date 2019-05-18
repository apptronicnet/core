package net.apptronic.core.base.utils

import net.apptronic.core.base.Logger
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.platform.PlatformHandler
import net.apptronic.core.threading.Scheduler

open class TestContext : Context {

    private val logger = Logger().apply {
        setupLogging {
            System.out.println(it)
        }
    }
    private val scheduler = TestScheduler()
    private val lifecycle = TestLifecycle()
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

    override fun getPlatformHandler(): PlatformHandler {
        throw NotImplementedError("Not used for tests")
    }

}