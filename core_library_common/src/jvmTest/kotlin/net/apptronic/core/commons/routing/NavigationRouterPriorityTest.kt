package net.apptronic.core.commons.routing

import net.apptronic.core.context.*
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.record
import org.junit.After
import org.junit.Test

class NavigationRouterPriorityTest {

    val context = coreContext {
        dependencyModule {
            navigationRouter()
        }
    }

    private class PriorityHandler(
            context: Context,
            override val navigationHandlerPriority: Int
    ) : Component(context), DefaultNavigationHandler {

        val events = typedEvent<Any>()

        init {
            registerNavigationHandler(this)
        }

        override fun onNavigationCommand(command: Any): Boolean {
            events.update(command)
            return true
        }

    }

    private val router = context.injectNavigationRouter()

    @Test
    fun testPriorityOrderDirect() {
        val handler1 = PriorityHandler(context.childContext(), 10000)
        val handler2 = PriorityHandler(context.childContext(), 1000)
        val handler3 = PriorityHandler(context.childContext(), 100)
        runPriorityTest(handler1, handler2, handler3)
    }

    @Test
    fun testPriorityOrderReversed() {
        val handler3 = PriorityHandler(context.childContext(), 100)
        val handler2 = PriorityHandler(context.childContext(), 1000)
        val handler1 = PriorityHandler(context.childContext(), 10000)
        runPriorityTest(handler1, handler2, handler3)
    }

    private fun runPriorityTest(handler1: PriorityHandler, handler2: PriorityHandler, handler3: PriorityHandler) {
        val events1 = handler1.events.record()
        val events2 = handler2.events.record()
        val events3 = handler3.events.record()

        router.sendCommandsSync(1)
        events1.assertItems(1)
        events2.assertItems()
        events3.assertItems()

        router.sendCommandsSync(2)

        events1.assertItems(1, 2)
        events2.assertItems()
        events3.assertItems()

        handler1.terminate()

        router.sendCommandsSync(3)
        events1.assertItems(1, 2)
        events2.assertItems(3)
        events3.assertItems()

        handler3.terminate()

        router.sendCommandsSync(4)
        events1.assertItems(1, 2)
        events2.assertItems(3, 4)
        events3.assertItems()

        handler2.terminate()
        router.sendCommandsSync(5)
        events1.assertItems(1, 2)
        events2.assertItems(3, 4)
        events3.assertItems()
    }

    @After
    fun after() {
        context.terminate()
    }

}