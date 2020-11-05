package net.apptronic.core.commons.routing

import net.apptronic.core.context.*
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.record
import org.junit.After
import org.junit.Test

class NavigationRouterOrderTest {

    val context = coreContext {
        dependencyModule {
            navigationRouter()
        }
    }

    private class SimpleHandler(
            context: Context,
    ) : Component(context), DefaultNavigationHandler {

        val events = typedEvent<Any>()

        init {
            registerNavigationHandler(this)
        }

        override fun onNavigationCommand(command: Any): Boolean {
            events.sendEvent(command)
            return true
        }

    }

    private val router = context.injectNavigationRouter()

    @Test
    fun verifyOrder() {
        val handler1 = SimpleHandler(context.childContext())
        val events1 = handler1.events.record()

        router.sendCommandsSync(1)
        events1.assertItems(1)

        val handler2 = SimpleHandler(context.childContext())
        val events2 = handler2.events.record()

        router.sendCommandsSync(2)
        events1.assertItems(1)
        events2.assertItems(2)

        router.sendCommandsSync(3)
        events1.assertItems(1)
        events2.assertItems(2, 3)

        val handler3 = SimpleHandler(context.childContext())
        val events3 = handler3.events.record()

        router.sendCommandsSync(4)
        events1.assertItems(1)
        events2.assertItems(2, 3)
        events3.assertItems(4)

        router.sendCommandsSync(5)
        events1.assertItems(1)
        events2.assertItems(2, 3)
        events3.assertItems(4, 5)

        handler3.terminate()

        router.sendCommandsSync(6, 7)
        events1.assertItems(1)
        events2.assertItems(2, 3, 6, 7)
        events3.assertItems(4, 5)
    }

    @After
    fun after() {
        context.terminate()
    }

}