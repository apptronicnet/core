package net.apptronic.core.base.utils

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.platform.TestPlatform
import net.apptronic.core.platform.initializePlatform
import net.apptronic.core.threading.Scheduler
import net.apptronic.core.threading.WorkerDefinition

val TestWorker = WorkerDefinition.DEFAULT

open class TestContext : Context {

    init {
        initializePlatform(TestPlatform)
    }

    private val scheduler = createTestScheduler()
    private val lifecycle = TestLifecycle()
    private val dependencyProvider = DependencyProvider(this, null)

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