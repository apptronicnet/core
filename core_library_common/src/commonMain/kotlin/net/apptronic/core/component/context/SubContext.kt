package net.apptronic.core.component.context

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.threading.Scheduler
import net.apptronic.core.threading.createSubScheduler

open class SubContext(
        private val parent: Context,
        private val lifecycle: Lifecycle = Lifecycle()
) : Context {

    override val defaultDispatcher: CoroutineDispatcher = parent.defaultDispatcher

    init {
        requireNeverFrozen()
        parent.getLifecycle().registerChildLifecycle(lifecycle)
    }

    private val dependencyProvider = DependencyProvider(this, parent.getProvider())
    private val scheduler = createSubScheduler(this, parent.getScheduler())

    override fun getParent(): Context? {
        return parent
    }

    final override fun getScheduler(): Scheduler {
        return scheduler
    }

    final override fun getProvider(): DependencyProvider {
        return dependencyProvider
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

}