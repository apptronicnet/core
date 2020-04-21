package net.apptronic.core.component.context

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.threading.Scheduler
import net.apptronic.core.threading.createCoreScheduler

open class CoreContext(
        private val lifecycle: Lifecycle = Lifecycle(),
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main
) : Context {

    override val defaultDispatcher: CoroutineDispatcher = coroutineDispatcher

    init {
        requireNeverFrozen()
    }

    private val scheduler = createCoreScheduler(this)
    private val dependencyProvider = DependencyProvider(this, null)

    override fun getParent(): Context? {
        return null
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