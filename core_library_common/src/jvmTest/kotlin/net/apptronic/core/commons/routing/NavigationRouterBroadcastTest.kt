package net.apptronic.core.commons.routing

import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.record
import net.apptronic.core.testutils.createTestContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.viewModelContext
import org.junit.Test

class NavigationRouterBroadcastTest {

    val coreContext = createTestContext()

    companion object {
        private fun Contextual.childViewModel() = ChildViewModel(viewModelContext())
    }

    class HostViewModel(context: ViewModelContext) : ViewModel(context), NavigationHandler<Any> {

        val navigationEvents = typedEvent<Any>()

        init {
            hostNavigationRouter()
            registerNavigationHandler(this)
        }

        val router = injectNavigationRouter()

        override fun onNavigationCommand(command: Any): Boolean {
            navigationEvents.update(command)
            return true
        }

        val child1 = childViewModel()
        val child2 = childViewModel()
        val childOfChild = child1.childViewModel()

    }

    class ChildViewModel(context: ViewModelContext) : ViewModel(context), NavigationHandler<Any> {

        val navigationEvents = typedEvent<Any>()

        init {
            registerNavigationHandler(this)
        }

        override fun onNavigationCommand(command: Any): Boolean {
            navigationEvents.update(command)
            return true
        }

    }

    @Test
    fun verifyBroadcastSentToAll() {
        val viewModel = HostViewModel(coreContext.viewModelContext())
        val eventsRoot = viewModel.navigationEvents.record()
        val eventsChild1 = viewModel.child1.navigationEvents.record()
        val eventsChild2 = viewModel.child2.navigationEvents.record()
        val eventsChildOfChild = viewModel.childOfChild.navigationEvents.record()
        viewModel.router.sendCommandsBroadcastSync(1)
        viewModel.router.sendCommandsBroadcastSync(2)
        viewModel.router.sendCommandsBroadcastSync(3)
        viewModel.router.sendCommandsBroadcastSync(4)
        eventsRoot.assertItems(1, 2, 3, 4)
        eventsChild1.assertItems(1, 2, 3, 4)
        eventsChild2.assertItems(1, 2, 3, 4)
        eventsChildOfChild.assertItems(1, 2, 3, 4)
    }

}