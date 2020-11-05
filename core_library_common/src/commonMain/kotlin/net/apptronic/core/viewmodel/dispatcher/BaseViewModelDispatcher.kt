package net.apptronic.core.viewmodel.dispatcher

import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.terminate
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelParent
import kotlin.reflect.KClass

/**
 * Component which handles root [ViewModel] for app UI. This [ViewModel] will be used as root for system UI container
 * can can be attached only to one container at same time. In case if app UI wants to destoroy
 */
abstract class BaseViewModelDispatcher<T : IViewModel>(
        context: Context,
        private val type: KClass<T>
) : Component(context), ViewModelDispatcher<T>, ViewModelParent {

    private var viewModel: T? = null
    private var viewContainer: ViewContainer<T>? = null

    override fun viewModelType(): KClass<T> {
        return type
    }

    override fun haveActiveViewModel(): Boolean {
        return viewModel != null
    }

    override fun getViewModel(): T {
        return viewModel ?: onCreateViewModelRequested().also {
            it.onAttachToParent(this)
            viewModel = it
        }
    }

    override fun registerContainer(container: ViewContainer<T>) {
        if (viewContainer != null) {
            throw IllegalStateException("UiContainer already set")
        }
        this.viewContainer = container
        val viewModel = getViewModel()
        container.onAddedViewModel(viewModel)
    }

    override fun unregisterContainer(container: ViewContainer<T>) {
        if (viewContainer == null) {
            throw IllegalStateException("UiContainer was not set")
        }
        if (viewContainer != container) {
            throw IllegalStateException("Should unregister same UiContainer")
        }
        viewContainer = null
    }

    abstract fun onCreateViewModelRequested(): T

    override fun recycleViewModel() {
        viewModel?.onDetachFromParent()
        viewModel?.terminate()
        viewModel = null
    }

    override fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any?) {
        viewContainer?.onViewModelRequestedCloseSelf()
    }

}