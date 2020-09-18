package net.apptronic.core.commons.navigation

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.typedEvent
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.record
import net.apptronic.core.testutils.testContext
import org.junit.Test

class NavigationRouterBroadcastTest {

    val coreContext = testContext()

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
            navigationEvents.sendEvent(command)
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
            navigationEvents.sendEvent(command)
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
        viewModel.router.sendCommandsBroadcast(1)
        viewModel.router.sendCommandsBroadcast(2)
        viewModel.router.sendCommandsBroadcast(3)
        viewModel.router.sendCommandsBroadcast(4)
        eventsRoot.assertItems(1, 2, 3, 4)
        eventsChild1.assertItems(1, 2, 3, 4)
        eventsChild2.assertItems(1, 2, 3, 4)
        eventsChildOfChild.assertItems(1, 2, 3, 4)
    }

}