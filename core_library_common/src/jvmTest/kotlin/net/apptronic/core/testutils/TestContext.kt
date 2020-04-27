package net.apptronic.core.testutils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.DependencyDispatcher
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.platform.TestPlatform
import net.apptronic.core.platform.initializePlatform

open class TestContext(
        private val parent: Context? = null,
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.Unconfined
) : Context {

    override val defaultDispatcher: CoroutineDispatcher = coroutineDispatcher

    init {
        initializePlatform(TestPlatform)
        parent?.getLifecycle()?.doOnTerminate {
            getLifecycle().terminate()
        }
    }

    private val lifecycle = TestLifecycle()
    private val dependencyProvider = DependencyDispatcher(this, null)

    override fun getParent(): Context? {
        return parent
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    override fun dependencyDispatcher(): DependencyDispatcher {
        return dependencyProvider
    }

}