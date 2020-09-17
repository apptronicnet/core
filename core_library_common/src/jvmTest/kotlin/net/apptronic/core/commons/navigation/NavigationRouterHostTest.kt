package net.apptronic.core.commons.navigation

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.dependencyModule
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.typedEvent
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.record
import net.apptronic.core.testutils.testContext
import org.junit.Test

class NavigationRouterHostTest {

    val coreContext = testContext()

    class HostViewModel(context: ViewModelContext) : ViewModel(context), NavigationCallback<Any> {

        val navigationEvents = typedEvent<Any>()

        init {
            hostNavigationRouter()
            registerNavigationCallback(this)
        }

        val router = injectNavigationRouter()

        override fun onNavigationEvent(event: Any): Boolean {
            navigationEvents.sendEvent(event)
            return true
        }

    }

    @Test
    fun verifyNavigationRouterHost() {
        val viewModel = HostViewModel(coreContext.viewModelContext())
        val events = viewModel.navigationEvents.record()
        viewModel.router.sendEvent(1)
        viewModel.router.sendEvent(2)
        viewModel.router.sendBroadcastEvent(3)
        events.assertItems(1, 2, 3)
    }

    private fun Contextual.dependencyViewModel() = DependencyViewModel(
            viewModelContext {
                dependencyModule {
                    hostNavigationRouter()
                }
            }
    )

    class DependencyViewModel(context: ViewModelContext) : ViewModel(context), NavigationCallback<Any> {

        val navigationEvents = typedEvent<Any>()

        init {
            registerNavigationCallback(this)
        }

        val router = injectNavigationRouter()

        override fun onNavigationEvent(event: Any): Boolean {
            navigationEvents.sendEvent(event)
            return true
        }

    }

    @Test
    fun verifyNavigationRouterDependency() {
        val viewModel = coreContext.dependencyViewModel()
        val events = viewModel.navigationEvents.record()
        viewModel.router.sendEvent(1)
        viewModel.router.sendEvent(2)
        viewModel.router.sendBroadcastEvent(3)
        events.assertItems(1, 2, 3)
    }

}