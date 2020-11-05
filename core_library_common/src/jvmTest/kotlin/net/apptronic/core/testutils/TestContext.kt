package net.apptronic.core.testutils

import net.apptronic.core.context.BaseContext
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.CoroutineDispatchers
import net.apptronic.core.context.di.DependencyDispatcher
import net.apptronic.core.context.di.dependencyDispatcher
import net.apptronic.core.test.testCoroutineDispatchers

fun createTestContext(
        parent: Context? = null,
        coroutineDispatchers: CoroutineDispatchers = testCoroutineDispatchers(),
        builder: Context.() -> Unit = {}): Context {
    return TestContext(parent, coroutineDispatchers).apply(builder)
}

private class TestContext(
        override val parent: Context? = null,
        override val coroutineDispatchers: CoroutineDispatchers = testCoroutineDispatchers()
) : BaseContext() {

    override val lifecycle = TEST_LIFECYCLE.createLifecycle()
    override val dependencyDispatcher: DependencyDispatcher = dependencyDispatcher()

    init {
        parent?.lifecycle?.registerChildLifecycle(lifecycle)
        plugins.attach(this)
    }

}