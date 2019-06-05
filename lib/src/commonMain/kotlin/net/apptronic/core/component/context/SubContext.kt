package net.apptronic.core.component.context

import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.threading.Scheduler
import net.apptronic.core.threading.createSubScheduler

open class SubContext(
        private val parent: Context,
        private val lifecycle: Lifecycle = Lifecycle()
) : Context {

    init {
        parent.getLifecycle().doOnTerminate {
            getLifecycle().terminate()
        }
    }

    private val dependencyProvider = DependencyProvider(this, parent.getProvider())
    private val scheduler = createSubScheduler(parent.getScheduler())

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