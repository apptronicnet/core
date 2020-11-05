package net.apptronic.core.commons.routing

import net.apptronic.core.context.*
import net.apptronic.core.context.component.Component
import org.junit.Test
import kotlin.test.assertEquals

class NavigationCallbackLifecycleTest {

    val context = coreContext {
        dependencyModule {
            navigationRouter()
        }
    }

    private class SimpleHandler(
            context: Context,
    ) : Component(context), DefaultNavigationHandler {

        var count = 0

        init {
            registerNavigationHandler(this)
        }

        override fun onNavigationCommand(command: Any): Boolean {
            count++
            return true
        }

    }

    private val router = context.injectNavigationRouter()

    @Test
    fun verifyLifecycle() {
        val handler1 = SimpleHandler(context.childContext())

        router.sendCommandsSync(1, 2, 3)
        assertEquals(handler1.count, 3)

        val handler2 = SimpleHandler(context.childContext())
        router.sendCommandsSync(4, 5)

        assertEquals(handler1.count, 3)
        assertEquals(handler2.count, 2)

        handler2.terminate()

        router.sendCommandsSync(6, 7)
        assertEquals(handler1.count, 5)
        assertEquals(handler2.count, 2)

        router.sendCommandsBroadcastSync(8, 9)
        assertEquals(handler1.count, 7)
        assertEquals(handler2.count, 2)

        handler1.terminate()

        router.sendCommandsBroadcastSync(10, 11, 12)
        assertEquals(handler1.count, 7)
        assertEquals(handler2.count, 2)
    }

}