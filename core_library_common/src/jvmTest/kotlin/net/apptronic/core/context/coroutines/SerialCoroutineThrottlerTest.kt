package net.apptronic.core.context.coroutines

import kotlinx.coroutines.CompletableDeferred
import net.apptronic.core.assertListEquals
import net.apptronic.core.context.component.Component
import net.apptronic.core.testutils.createTestContext
import org.junit.Test

class SerialCoroutineThrottlerTest {

    val component = Component(createTestContext())

    @Test
    fun shouldExecuteSerially() {
        val throttler = component.contextCoroutineScope.serialThrottler()
        val await1 = CompletableDeferred<Unit>()
        val await2 = CompletableDeferred<Unit>()
        val await3 = CompletableDeferred<Unit>()
        val invocations = mutableListOf<String>()
        throttler.launch {
            invocations.add("pre1")
            await1.await()
            invocations.add("post1")
        }
        throttler.launch {
            invocations.add("pre2")
            await2.await()
            invocations.add("post2")
        }
        throttler.launch {
            invocations.add("pre3")
            await3.await()
            invocations.add("post3")
        }
        assertListEquals(invocations, listOf("pre1"))
        await3.complete(Unit)
        assertListEquals(invocations, listOf("pre1"))
        await2.complete(Unit)
        assertListEquals(invocations, listOf("pre1"))
        await1.complete(Unit)
        assertListEquals(invocations, listOf("pre1", "post1", "pre2", "post2", "pre3", "post3"))
    }

}