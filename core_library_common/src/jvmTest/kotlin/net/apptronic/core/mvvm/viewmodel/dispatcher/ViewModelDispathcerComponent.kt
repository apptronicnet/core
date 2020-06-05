package net.apptronic.core.mvvm.viewmodel.dispatcher

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.typedEvent
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelLifecycleController
import net.apptronic.core.testutils.TestContext
import kotlin.reflect.KClass

inline fun <reified T : ViewModel> viewModelDispathcerComponent(noinline builder: (Context) -> T): ViewModelDispathcerComponent<T> {
    return ViewModelDispathcerComponent(T::class, builder)
}

class ViewModelDispathcerComponent<T : ViewModel>(
        private val viewModelClass: KClass<T>,
        private val builder: (Context) -> T
) : BaseComponent(TestContext()) {

    val dispatcher = BuilderViewModelDispatcher(context, viewModelClass, builder)

    private var container: TestViewContainer? = null

    fun attachUi() {
        container = TestViewContainer()
        dispatcher.registerContainer(container!!)
        setCreated(true)
    }

    fun detachUi() {
        dispatcher.unregisterContainer(container!!)
        container = null
    }

    inner class TestViewContainer : ViewContainer<T> {

        override fun onAddedViewModel(viewModel: T) {
            onAdded.update(viewModel)
            lifecycleController = ViewModelLifecycleController(viewModel)
        }

        override fun onViewModelRequestedCloseSelf() {
            closeSelfEvent.sendEvent()
            dispatcher.recycleViewModel()
            detachUi()
        }

    }

    val onAdded = typedEvent<T>()
    val closeSelfEvent = genericEvent()

    private var lifecycleController: ViewModelLifecycleController? = null

    fun setCreated(isCreated: Boolean) {
        lifecycleController!!.setBound(isCreated)
    }

    fun setBound(isBound: Boolean) {
        lifecycleController!!.setBound(isBound)
    }

    fun setVisible(isVisible: Boolean) {
        lifecycleController!!.setVisible(isVisible)
    }

    fun setFocused(isFocused: Boolean) {
        lifecycleController!!.setFocused(isFocused)
    }

    fun fullUnbind() {
        setFocused(false)
        setVisible(false)
        setBound(false)
    }

    fun fullFocus() {
        setBound(true)
        setVisible(true)
        setFocused(true)
    }

    fun simulateRebind() {
        fullUnbind()
        fullFocus()
    }

}