package net.apptronic.core.testutils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.apptronic.core.component.context.BaseContext
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.DependencyDispatcher

open class TestContext(
        override val parent: Context? = null,
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.Unconfined
) : BaseContext() {

    override val defaultDispatcher: CoroutineDispatcher = coroutineDispatcher

    init {
        parent?.lifecycle?.doOnTerminate {
            lifecycle.terminate()
        }
    }

    override val lifecycle = TEST_LIFECYCLE.createLifecycle()
    override val dependencyDispatcher = DependencyDispatcher(this, null)

}