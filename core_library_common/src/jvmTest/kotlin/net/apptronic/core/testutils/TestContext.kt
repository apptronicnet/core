package net.apptronic.core.testutils

import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.context.coreContext
import net.apptronic.core.context.coroutines.CoroutineDispatchers
import net.apptronic.core.test.testCoroutineDispatchers

fun createTestContext(
    parent: Context? = null,
    coroutineDispatchers: CoroutineDispatchers = testCoroutineDispatchers(),
    builder: Context.() -> Unit = {}
): Context {
    return (
            parent?.childContext(builder)
                ?: coreContext(coroutineDispatchers = coroutineDispatchers, builder)
            )
        .apply {
            TestLifecycleDefinition.assignTo(context)
        }
}
