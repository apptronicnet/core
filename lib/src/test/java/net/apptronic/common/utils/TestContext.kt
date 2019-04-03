package net.apptronic.common.utils

import net.apptronic.core.base.ComponentLogger
import net.apptronic.core.base.ComponentLoggerDescriptor
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.threading.ContextWorkers

open class TestContext : Context {

    private val workers = TestWorkers()
    private val lifecycle = TestLifecycle(workers)
    private val diContext = DependencyProvider(this, null)

    init {
        getProvider().addInstance(ComponentLoggerDescriptor, ComponentLogger().apply {
            logMethod = {
                System.out.println(it)
            }
        })
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    override fun getWorkers(): ContextWorkers {
        return workers
    }

    override fun getProvider(): DependencyProvider {
        return diContext
    }

}