package net.apptronic.core.commons.routing

import net.apptronic.core.context.*
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.record
import org.junit.After
import org.junit.Test

class MultipleNavigationRoutersTest {

    private val intDescriptor = navigationRouterDescriptor<Int>()
    private val stringDescriptor = navigationRouterDescriptor<String>()

    val context = coreContext {
        dependencyModule {
            navigationRouter(intDescriptor)
            navigationRouter(stringDescriptor)
        }
    }

    private class TypedHandler<T>(
            context: Context, routerDescriptor: NavigationRouterDescriptor<T>
    ) : Component(context), NavigationHandler<T> {

        val events = typedEvent<T>()

        init {
            registerNavigationHandler(routerDescriptor, this)
        }

        override fun onNavigationCommand(command: T): Boolean {
            events.update(command)
            return true
        }

    }

    private val intRouter = context.injectNavigationRouter(intDescriptor)
    private val stringRouter = context.injectNavigationRouter(stringDescriptor)

    @Test
    fun notWrittenYet() {
        val intHandler = TypedHandler(context.childContext(), intDescriptor)
        val stringHandler = TypedHandler(context.childContext(), stringDescriptor)

        val intRecords = intHandler.events.record()
        val stringRecords = stringHandler.events.record()

        intRouter.sendCommandsSync(1, 2)
        intRecords.assertItems(1, 2)

        stringRouter.sendCommandsSync("A", "B")
        stringRecords.assertItems("A", "B")

        intRouter.sendCommandsSync(3, 4, 5)
        intRecords.assertItems(1, 2, 3, 4, 5)
        stringRecords.assertItems("A", "B")
    }

    @After
    fun after() {
        context.terminate()
    }

}