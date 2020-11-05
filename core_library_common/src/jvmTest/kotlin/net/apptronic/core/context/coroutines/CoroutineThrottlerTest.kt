package net.apptronic.core.context.coroutines

import kotlinx.coroutines.CompletableDeferred
import net.apptronic.core.context.component.Component
import net.apptronic.core.testutils.createTestContext
import org.junit.Test

class CoroutineThrottlerTest {

    val component = Component(createTestContext())

    @Test
    fun shouldThrottleCorrectly() {
        val throttler = component.contextCoroutineScope.throttler()
        val await = CompletableDeferred<Unit>()
        var preInvoke1 = false
        var postInvoke1 = false
        throttler.launch {
            preInvoke1 = true
            await.await()
            postInvoke1 = true
        }
        assert(preInvoke1)
        assert(!postInvoke1)

        var invoke2 = false
        throttler.launch {
            invoke2 = true
        }
        assert(preInvoke1)
        assert(!postInvoke1)
        assert(!invoke2)

        var invoke3 = false
        throttler.launch {
            invoke3 = true
        }
        assert(preInvoke1)
        assert(!postInvoke1)
        assert(!invoke2)
        assert(!invoke3)

        await.complete(Unit)
        assert(preInvoke1)
        assert(postInvoke1)
        assert(!invoke2)
        assert(invoke3)
    }

}