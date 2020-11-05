package net.apptronic.core.commons.routing

import net.apptronic.core.context.Contextual
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.record
import net.apptronic.core.testutils.createTestContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.viewModelContext
import org.junit.Test

class NavigationRouterHostTest {

    val coreContext = createTestContext()

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

    }

    @Test
    fun verifyNavigationRouterHost() {
        val viewModel = HostViewModel(coreContext.viewModelContext())
        val events = viewModel.navigationEvents.record()
        viewModel.router.sendCommandsSync(1)
        viewModel.router.sendCommandsSync(2)
        viewModel.router.sendCommandsBroadcastSync(3)
        events.assertItems(1, 2, 3)
    }

    private fun Contextual.dependencyViewModel() = DependencyViewModel(
            viewModelContext {
                dependencyModule {
                    navigationRouter()
                }
            }
    )

    class DependencyViewModel(context: ViewModelContext) : ViewModel(context), NavigationHandler<Any> {

        val navigationEvents = typedEvent<Any>()

        init {
            registerNavigationHandler(this)
        }

        val router = injectNavigationRouter()

        override fun onNavigationCommand(command: Any): Boolean {
            navigationEvents.sendEvent(command)
            return true
        }

    }

    @Test
    fun verifyNavigationRouterDependency() {
        val viewModel = coreContext.dependencyViewModel()
        val events = viewModel.navigationEvents.record()
        viewModel.router.sendCommandsSync(1)
        viewModel.router.sendCommandsSync(2)
        viewModel.router.sendCommandsBroadcastSync(3)
        events.assertItems(1, 2, 3)
    }

}