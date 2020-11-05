package net.apptronic.core.test

import net.apptronic.core.context.Context
import net.apptronic.core.context.coreContext
import net.apptronic.core.context.coroutines.CoroutineDispatchers

/**
 * Create instance of [Context] for testing
 */
fun testContext(
        coroutineDispatchers: CoroutineDispatchers = testCoroutineDispatchers(),
        builder: Context.() -> Unit = {}): Context {
    return coreContext(coroutineDispatchers, builder)
}