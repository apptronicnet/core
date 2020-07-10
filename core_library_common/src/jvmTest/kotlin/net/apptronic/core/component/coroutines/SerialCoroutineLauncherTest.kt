package net.apptronic.core.component.coroutines

import kotlinx.coroutines.CompletableDeferred
import net.apptronic.core.assertListEquals
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.testutils.testContext
import org.junit.Test

class SerialCoroutineLauncherTest {

    val component = BaseComponent(testContext())

    @Test
    fun shouldExecuteSerially() {
        val coroutineLauncher = component.coroutineLaunchers().local.serial()
        val await1 = CompletableDeferred<Unit>()
        val await2 = CompletableDeferred<Unit>()
        val await3 = CompletableDeferred<Unit>()
        val invocations = mutableListOf<String>()
        coroutineLauncher.launch {
            invocations.add("pre1")
            await1.await()
            invocations.add("post1")
        }
        coroutineLauncher.launch {
            invocations.add("pre2")
            await2.await()
            invocations.add("post2")
        }
        coroutineLauncher.launch {
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