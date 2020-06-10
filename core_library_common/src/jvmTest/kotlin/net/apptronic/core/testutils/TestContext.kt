package net.apptronic.core.testutils

import kotlinx.coroutines.Dispatchers
import net.apptronic.core.component.context.BaseContext
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.CoroutineDispatchers
import net.apptronic.core.component.di.DependencyDispatcher

fun testCoroutineDispatchers(): CoroutineDispatchers {
    return CoroutineDispatchers(Dispatchers.Unconfined)
}

fun testContext(
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
    override val dependencyDispatcher = DependencyDispatcher(this, parent?.dependencyDispatcher)

    init {
        parent?.lifecycle?.registerChildLifecycle(lifecycle)
    }

}