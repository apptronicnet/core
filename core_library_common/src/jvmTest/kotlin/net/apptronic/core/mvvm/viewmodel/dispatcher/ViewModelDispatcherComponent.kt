package net.apptronic.core.mvvm.viewmodel.dispatcher

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.typedEvent
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelLifecycleController
import net.apptronic.core.testutils.testContext
import kotlin.reflect.KClass

inline fun <reified T : IViewModel> viewModelDispathcerComponent(noinline builder: (Context) -> T): ViewModelDispatcherComponent<T> {
    return ViewModelDispatcherComponent(T::class, builder)
}

class ViewModelDispatcherComponent<T : IViewModel>(
        private val viewModelClass: KClass<T>,
        private val builder: (Context) -> T
) : Component(testContext()) {

    val dispatcher = BuilderViewModelDispatcher(context, viewModelClass, builder)

    private var container: TestViewContainer? = null

    fun attachUi() {
        container = TestViewContainer()
        dispatcher.registerContainer(container!!)
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