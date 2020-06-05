package net.apptronic.core.component.coroutines

import kotlinx.coroutines.CompletableDeferred
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.testutils.TestContext
import org.junit.Test

class CoroutineThrottlerTest {

    val component = BaseComponent(TestContext())

    @Test
    fun shouldThrottleCorrectly() {
        val coroutineLauncher = component.coroutineLaunchers().local.throttler()
        val await = CompletableDeferred<Unit>()
        var preInvoke1 = false
        var postInvoke1 = false
        coroutineLauncher.launch {
            preInvoke1 = true
            await.await()
            postInvoke1 = true
        }
        assert(preInvoke1)
        assert(!postInvoke1)

        var invoke2 = false
        coroutineLauncher.launch {
            invoke2 = true
        }
        assert(preInvoke1)
        assert(!postInvoke1)
        assert(!invoke2)

        var invoke3 = false
        coroutineLauncher.launch {
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