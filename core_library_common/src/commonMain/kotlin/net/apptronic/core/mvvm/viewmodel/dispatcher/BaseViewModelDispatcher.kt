package net.apptronic.core.mvvm.viewmodel.dispatcher

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.close
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelParent

/**
 * Component which handles root [ViewModel] for app UI. This [ViewModel] will be used as root for system UI container
 * can can be attached only to one container at same time. In case if app UI wants to destoroy
 */
abstract class BaseViewModelDispatcher<T : ViewModel>(
        override val context: Context
) : Component(), ViewModelDispatcher<T>, ViewModelParent {

    private var viewModel: T? = null
    private var uiContainer: UiContainer<T>? = null

    override fun getViewModel(): T {
        return viewModel ?: onCreateViewModelRequested().also {
            viewModel = it
        }
    }

    override fun registerContainer(container: UiContainer<T>) {
        if (uiContainer != null) {
            throw IllegalStateException("UiContainer already set")
        }
        this.uiContainer = container
        val viewModel = getViewModel()
        container.onAddedViewModel(viewModel)
    }

    override fun unregisterContainer(container: UiContainer<T>) {
        if (uiContainer == null) {
            throw IllegalStateException("UiContainer was not set")
        }
        if (uiContainer != container) {
            throw IllegalStateException("Should unregister same UiContainer")
        }
        uiContainer = null
    }

    abstract fun onCreateViewModelRequested(): T

    override fun recycleViewModel() {
        viewModel?.context?.close()
        viewModel = null
    }

    override fun requestCloseSelf(viewModel: ViewModel, transitionInfo: Any?) {
        uiContainer?.onViewModelRequestedCloseSelf()
    }

}