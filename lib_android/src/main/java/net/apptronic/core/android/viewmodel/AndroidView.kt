package net.apptronic.core.android.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class AndroidView<T : ViewModel> {

    @LayoutRes
    var layoutResId: Int? = null

    fun onCreateView(container: ViewGroup): View {
        return layoutResId?.let {
            LayoutInflater.from(container.context).inflate(it, container, false)
        } ?: throw IllegalStateException("[layoutResId] is not specified for $this")
    }

    internal fun requestBinding(view: View, viewModel: ViewModel): ViewModelBinding<*> {
        return onCreateBinding(view, viewModel as T).apply {
            bind()
        }
    }

    abstract fun onCreateBinding(view: View, viewModel: T): ViewModelBinding<T>

    fun createBinding(
        view: View,
        viewModel: T,
        bindingAction: ViewModelBinding<T>.() -> Unit
    ): ViewModelBinding<T> {
        return DefaultBinding(view, viewModel, bindingAction)
    }

    private class DefaultBinding<T : ViewModel>(
        view: View, viewModel: T,
        private val bindingAction: ViewModelBinding<T>.() -> Unit
    ) : ViewModelBinding<T>(view, viewModel) {

        override fun onBind() {
            bindingAction.invoke(this)
        }

    }

}

