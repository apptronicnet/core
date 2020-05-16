package net.apptronic.core.mvvm.viewmodel.dispatcher

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.close
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelParent

/**
 * Component which handles root [ViewModel] for app UI. This [ViewModel] will be used as root for system UI container
 * can can be attached only to one container at same time. In case if app UI wants to destoroy
 */
abstract class BaseViewModelDispatcher<T : ViewModel>(
        context: Context
) : BaseComponent(context), ViewModelDispatcher<T>, ViewModelParent {

    private var viewModel: T? = null
    private var viewContainer: ViewContainer<T>? = null

    override fun haveActiveViewModel(): Boolean {
        return viewModel != null
    }

    override fun getViewModel(): T {
        return viewModel ?: onCreateViewModelRequested().also {
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
        viewModel?.context?.close()
        viewModel = null
    }

    override fun requestCloseSelf(viewModel: ViewModel, transitionInfo: Any?) {
        viewContainer?.onViewModelRequestedCloseSelf()
    }

}