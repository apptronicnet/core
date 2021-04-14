package net.apptronic.core.viewmodel.dispatcher

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.testutils.createTestContext
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.ViewModelLifecycleController
import kotlin.reflect.KClass

inline fun <reified T : IViewModel> viewModelDispatcher(noinline builder: (Contextual) -> T): ViewModelDispatcherComponent<T> {
    return ViewModelDispatcherComponent(T::class, builder)
}

class ViewModelDispatcherComponent<T : IViewModel>(
        private val viewModelClass: KClass<T>,
        private val builder: (Context) -> T
) : Component(createTestContext()) {

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
            closeSelfEvent.update()
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